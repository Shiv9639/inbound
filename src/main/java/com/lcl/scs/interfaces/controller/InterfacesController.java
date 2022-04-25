package com.lcl.scs.interfaces.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.lcl.scs.interfaces.model.Interfaces;
import com.lcl.scs.util.CSVReader;
import com.lcl.scs.util.logging.LoggingUtilities;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


@RestController
@RequestMapping("/interfaces")
@ComponentScan(basePackages = "com.lcl.scs.interfaces")
public class InterfacesController {
	@Autowired
	private com.lcl.scs.interfaces.service.InterfacesService interfacesService;

	private static int PRETTY_PRINT_INDENT_FACTOR = 4;

	@Value("${spring.data.mongodb.uri}")
	private String mongodbURI;
	@Value("${spring.data.mongodb.database}")
	private String mongodbDatabase;

	@PostMapping(value = "/")
	public List<Interfaces> getAllInterfaces() {
		return interfacesService.findAll();
	}

	@PostMapping(value = "/byRicefnum", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public Interfaces getInterfaceByRicefNum(@RequestHeader("ricefnum") String ricefnum) {
		// System.out.println("Parameter: "+ricefnum);
		LoggingUtilities.generateInfoLog("Parameter: " + ricefnum);
		return interfacesService.findByRicefnum(ricefnum);
	}

	@PostMapping(value = "/bySource", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Interfaces> getInterfaceBySource(@RequestHeader("source") String source) {
		List<Interfaces> result = interfacesService.findBySource(source);
		System.out.println("Size: " + result.size());

		for (Interfaces i : result) {
			System.out.println("hello: " + i.toString());
		}

		return result;
	}

	@PostMapping(value = "/save")
	public ResponseEntity<?> saveOrUpdateInterfaces(@RequestHeader("ricefnum") String ricefNum,
			@RequestHeader("source") String source) {
		Interfaces i = new Interfaces();
		i.setRicefnum(ricefNum);
		i.setSource(source);
		interfacesService.saveOrUpdateInterfaces(i);
		return new ResponseEntity("Added successfully", HttpStatus.OK);
	}

	@PostMapping(value = { "/loadCSVFileByInterface" }, produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> loadCSVFileByInterface(@RequestHeader("interface") String interfaceNumber,
			HttpServletRequest request) {
		MongoClient mongoClient = null;
		try {
			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

			String datetime = simpleDateFormat.format(new Date());

			try {
				CSVReader csv = new CSVReader(true, ',', request.getInputStream());
				List<String> fieldNames = null;
				if (csv.hasNext())
					fieldNames = new ArrayList<>(csv.next());
				List<Map<String, String>> list = new ArrayList<>();
				while (csv.hasNext()) {
					List<String> x = csv.next();
					Map<String, String> obj = new LinkedHashMap<>();
					for (int i = 0; i < fieldNames.size(); i++) {
						obj.put(fieldNames.get(i), x.get(i));
					}
					list.add(obj);
				}

				List<Map<String, List>> documentList = new ArrayList<>();
				Map<String, List> fileContent = new LinkedHashMap<>();
				fileContent.put("File_Content", list);
				documentList.add(fileContent);

				csv.close();
				Gson gson = new Gson();
				String jsonString = gson.toJson(documentList);

				String estTimeZone = "Canada/Eastern";
				Document doc = Document.parse(jsonString.substring(1, jsonString.length() - 1));
//				doc.put("_id", interfaceNumber + "-" + datetime);
				doc.put("Name", interfaceNumber + "-" + datetime);

				Date date = new Date();
				SimpleDateFormat DATETIMEFORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
//				ZonedDateTime
//				.ofInstant(java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Canada/Eastern")).toInstant(), ZoneId.of("Canada/Eastern"));
				doc.put("Loading_Time",
						java.util.Date.from(ZonedDateTime
								.ofInstant(DATETIMEFORMATTER.parse(DATETIMEFORMATTER.format(date)).toInstant(),
										ZoneId.of("Canada/Eastern"))
								.toInstant()));

				mongoClient = MongoClients.create(mongodbURI);

				mongoClient.getDatabase(mongodbDatabase).getCollection(interfaceNumber).insertOne(doc);

				return new ResponseEntity(interfaceNumber + ": added successfully", HttpStatus.OK);
			} catch (Exception ex) {
				LoggingUtilities.generateErrorLog("Fail to post " + interfaceNumber + ": " + ex.getMessage());
				return new ResponseEntity("Fail to post " + interfaceNumber, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (mongoClient != null)
				mongoClient.close();
		}
	}

	@PostMapping(value = { "/loadMessageByInterfac", "/loadMessageByInterface" }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> loadMessageByInterface(@RequestHeader("interface") String interfaceNumber,
			@RequestHeader("Content-Type") String contentType, @RequestBody String messageBody) {
		MongoClient mongoClient = null;
		try {
			Interfaces result = interfacesService.findByRicefnum(interfaceNumber);
			
			if (result == null) {
				LoggingUtilities.generateErrorLog("Unrecognize interface " + interfaceNumber);
				return new ResponseEntity("Unrecognize interface " + interfaceNumber, HttpStatus.BAD_REQUEST);
			}

			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

			String datetime = simpleDateFormat.format(new Date());

			if (!contentType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE)
					&& !contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
				LoggingUtilities.generateErrorLog(
						"Fail to post " + interfaceNumber + ": the message is neither XML nor JSON! ");
				return new ResponseEntity("Fail to post " + interfaceNumber + ": the message is neither XML nor JSON! ",
						HttpStatus.BAD_REQUEST);
			}

			String jsonPrettyPrintString = null;

			if (contentType.equalsIgnoreCase(MediaType.APPLICATION_XML_VALUE))
				try {
					// test if the message is XML
					XML.toJSONObject(messageBody);
					JSONObject xmlJSONObj = XML.toJSONObject(messageBody);
					jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				} catch (Exception xmlex) {
					LoggingUtilities.generateErrorLog("Fail to post " + interfaceNumber
							+ ": the message is not XML while the content type is set to XML! " + xmlex.getMessage());
					return new ResponseEntity(
							"Fail to post " + interfaceNumber
									+ ": the message is not XML while the content type is set to XML! ",
							HttpStatus.BAD_REQUEST);
				}

			if (contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
				try {
					// test if the message is JSON
					Document.parse(messageBody);
					jsonPrettyPrintString = messageBody;
				} catch (Exception jsonex) {
					LoggingUtilities.generateErrorLog("Fail to post " + interfaceNumber
							+ ": the message is not JSON while the content type is set to JSON!" + jsonex.getMessage());
					return new ResponseEntity(
							"Fail to post " + interfaceNumber
									+ ": the message is not JSON while the content type is set to JSON! ",
							HttpStatus.BAD_REQUEST);
				}
			}

			if (jsonPrettyPrintString == null) {
				LoggingUtilities.generateErrorLog(
						"Fail to post " + interfaceNumber + ": the message is neither XML nor JSON! ");
				return new ResponseEntity("Fail to post " + interfaceNumber + ": the message is neither XML nor JSON! ",
						HttpStatus.BAD_REQUEST);
			}
				
			
			String estTimeZone = "Canada/Eastern";
			Document doc = Document.parse(jsonPrettyPrintString);
//			doc.put("_id", interfaceNumber + "-" + datetime);
			if(result.getEncodingType() != null && result.getEncodingType().equals("uriencoded")) {
				if (doc.getString("encodedData") != null) {
					try {
						String decodedValue = URLDecoder.decode(doc.getString("encodedData"), StandardCharsets.UTF_8.toString());	
						doc = Document.parse(decodedValue);
					} catch(Exception e) {
						LoggingUtilities.generateErrorLog(
								"Unable to decode: " + e.getMessage());
					}
				} else {
					LoggingUtilities.generateErrorLog(
							"Unable to decode, the value is NULL");
				}
			}
			doc.put("Name", interfaceNumber + "-" + datetime);
			Date date = new Date();
			SimpleDateFormat DATETIMEFORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
//			ZonedDateTime
//			.ofInstant(java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("Canada/Eastern")).toInstant(), ZoneId.of("Canada/Eastern"));
			doc.put("Loading_Time",
					java.util.Date.from(
							ZonedDateTime.ofInstant(DATETIMEFORMATTER.parse(DATETIMEFORMATTER.format(date)).toInstant(),
									ZoneId.of("Canada/Eastern")).toInstant()));
			// doc.put("Loading_Time", LocalDateTime.now());

			mongoClient = MongoClients.create(mongodbURI);

			mongoClient.getDatabase(mongodbDatabase).getCollection(interfaceNumber).insertOne(doc);

			return new ResponseEntity(interfaceNumber + ": added successfully", HttpStatus.OK);
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog("Fail to post " + interfaceNumber + ": " + ex.getMessage());
			return new ResponseEntity("Fail to post " + interfaceNumber, HttpStatus.BAD_REQUEST);
		} finally {
			if (mongoClient != null)
				mongoClient.close();
		}
	}
}
