package com.lcl.scs.subscribers.datacopier.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lcl.scs.subscribers.model.Subscribers;
import com.lcl.scs.util.logging.LoggingUtilities;
import com.lcl.scs.subscribers.model.ApigeeToken;
import com.lcl.scs.subscribers.constants.ApigeeConstants.APIGEE_TOKEN;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;


@Component
public class DataCopierScheduler {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	@Value("${spring.data.mongodb.uri}")
	private String mongodbURI;
	@Value("${spring.data.mongodb.database}")
	private String mongodbDatabase;

	@Autowired
	private com.lcl.scs.gcp.service.impl.SubscribersService subscribersService;

	private int LAST_RUN_TIME_BUFFER = -15;
	private String CONTENT_TYPE = "application/json";

	//@Scheduled(fixedDelay = 1000 * 60 * 1)
	//@SchedulerLock(name = "DataCopierScheduler", lockAtLeastFor = "PT5M", lockAtMostFor = "PT5M")
	public void copyData() {
		LoggingUtilities.generateInfoLog(dateFormat.format(new Date()) + ": Start copying all new data");
		
		MongoClient mongo = null;
		Date tempTime = new Date();
		try {
			mongo = MongoClients.create(mongodbURI);

			List<Subscribers> subscribersList = subscribersService.findBySubscribername("SCS-LE");
			for (Subscribers s : subscribersList) {
//				try {
					if (s.getLastrun() == null)
						continue;
				
					Date lastRunTime = s.getLastrun();
					s.setLastrun(null);
					s.setStatus("Running");
					subscribersService.save(s);
					LoggingUtilities.generateInfoLog("Subsciber saved with Last run null at :" + lastRunTime);
					BasicDBObject query = new BasicDBObject();

					Calendar c = Calendar.getInstance();
					c.setTime(lastRunTime);
					c.add(Calendar.SECOND, LAST_RUN_TIME_BUFFER);
					Date previousRunTime = c.getTime();
					query.put("Loading_Time", new BasicDBObject("$gte", previousRunTime));

					MongoCollection<Document> inbound_collection = mongo.getDatabase(mongodbDatabase)
							.getCollection(s.getInboundcollection());

					FindIterable<Document> documents = inbound_collection.find(query).sort(new BasicDBObject("_id", 1));
					ApigeeToken token = getApigeeToken();
					for (Document doc : documents) {
					
//						try {
						if (token != null) {
							LoggingUtilities.generateInfoLog("Processing R9333 collection payload with document _id " + doc.get("_id"));
							postDocument(s.getOutboundcollection(), encodeJson(doc), s.getOutboundEndpoint(), token);
							
						} 
//					catch (Exception e) {
					else {
						postDocument(s.getOutboundcollection(), encodeJson(doc), s.getOutboundEndpoint(), getApigeeToken());
//							LoggingUtilities.generateInfoLog("Process failed R9333 collection payload with document _id " + doc.get("_id"));
//							e.printStackTrace();
						}

					}

					s.setStatus("Stopped");
					s.setLastrun(tempTime);
					subscribersService.save(s);
					
//				} catch (Exception e) {
//					s.setStatus("Stopped");
//					s.setLastrun(tempTime);
//					subscribersService.save(s);
//					LoggingUtilities.generateErrorLog("Payload failed to process ");
//					e.printStackTrace();
//				}
			}
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog("Failed to copy data! " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mongo != null)
				mongo.close();
		}
		LoggingUtilities.generateInfoLog(dateFormat.format(new Date()) + ": Finish coping all new data");
	}
	
	public String encodeJson(Document docu) throws Exception, ParseException {
		Document temp = (Document) docu.get("NS1:SapOrders05Zorders0501");
		temp.remove("xmlns:NS1");
		String obj = docu.toJson();
		String encodedString = "";
		
		if (obj.startsWith("{{")) {
			encodedString = obj.replaceFirst("{{", "{").replace(";", "Ow==").replace("+", "Kw==")
			.replace("*", "Kg==").replace("@", "QA==").replace("/", "Lw==").replace("\\\"\"", "XA==\"").replace("\\","XA==")
			.replace("&","Jq==").replace("(","KA==").replace(")","KQ==").replace("<","PA==").replace(">","Pq==").replace("^","Xg==");
			
			if (obj.endsWith("}}")) {
				encodedString = encodedString.substring(0,encodedString.length()-1);
			}
		}

		else {
			encodedString = obj.replace(";", "Ow==").replace("+", "Kw==")
			.replace("*", "Kg==").replace("@", "QA==").replace("/", "Lw==").replace("\\\"\"", "XA==\"").replace("\\", "XA==")
			.replace("&","Jq==").replace("(","KA==").replace(")","KQ==").replace("<","PA==").replace(">","Pq==").replace("^","Xg==");

		}
		
		return (encodedString);
	}
	
	private ApigeeToken getApigeeToken() throws Exception {
		LoggingUtilities.generateInfoLog("generating Apigee token");
		ApigeeTokenServiceImpl tokenService = new ApigeeTokenServiceImpl(APIGEE_TOKEN.URL.getValue(),
				APIGEE_TOKEN.CLIENT_ID.getValue(), APIGEE_TOKEN.CLIENT_SECRET.getValue(), APIGEE_TOKEN.GRANT_TYPE.getValue(),
				APIGEE_TOKEN.SCOPE.getValue());

		return tokenService.getApigeeToken();
	}
	
	private void postDocument(String interfaceName, String body, String copyToURL, ApigeeToken token) throws Exception {
		try {
			HttpClient client = HttpClientBuilder.create().build();

			HttpPost post = new HttpPost(copyToURL);
			post.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
			post.setHeader("interface", interfaceName);
			
			post.setHeader(HttpHeaders.AUTHORIZATION, token.getToken_type() + " " + token.getAccess_token());
			
			post.setEntity(EntityBuilder.create().setText(body).build());

			HttpResponse response = client.execute(post);

			int code = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseStr = EntityUtils.toString(responseEntity);

			LoggingUtilities.generateInfoLog("Response: " + responseStr);

			if (code != 200) {
				LoggingUtilities.generateInfoLog("LCT API Response: " + responseStr + " The response code is " + code);
				throw new Exception("Invalid Response from " + copyToURL);
			}
			if (code == 401) {
				LoggingUtilities.generateInfoLog("Invalid Apigee Token");
				ApigeeToken newToken = getApigeeToken();
				post.setHeader(HttpHeaders.AUTHORIZATION, newToken.getToken_type() + " " + newToken.getAccess_token());
				HttpResponse responses = client.execute(post);
				HttpEntity responsesEntity = responses.getEntity();
				String responsesStr = EntityUtils.toString(responsesEntity);
				LoggingUtilities.generateInfoLog("Response: " + responsesStr);
				
			}

		} catch (Exception ex) {
			throw ex;
		}
	}
}
