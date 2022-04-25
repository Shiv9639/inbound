package com.lcl.scs.interfaces.service.impl;

import com.google.gson.Gson;
import com.lcl.scs.gcp.model.GcpFile;
import com.lcl.scs.gcp.service.impl.GcpService;
import com.lcl.scs.interfaces.service.LoadFileToMongoService;
import com.lcl.scs.util.CSVReader;
import com.lcl.scs.util.logging.LoggingUtilities;
import com.mongodb.client.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.springframework.stereotype.Service;

import org.json.JSONObject;
import org.json.XML;

@Service
public class LoadFileToMongoServiceImpl implements LoadFileToMongoService {

	private static int PRETTY_PRINT_INDENT_FACTOR = 4;

	private String mongodbURI;
	private String mongodbDatabase;

	private MongoClient mongoClient;

	public LoadFileToMongoServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
		try {
			mongoClient = MongoClients.create(mongodbURI);
			
		} catch (Exception ex) {
			;
		}
	}
	public void createClient(String mongodbURI,String mongodbDatabase) {
		this.mongodbURI = mongodbURI;
		this.mongodbDatabase  = mongodbDatabase;
		mongoClient = MongoClients.create(mongodbURI);
		
	}

	@Override
	public void destory() throws Exception {
		// TODO Auto-generated method stub
		try {
			if (mongoClient != null)
				mongoClient.close();
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
	}

	public String getMongodbDatabase() {
		return mongodbDatabase;
	}

	public String getMongodbURI() {
		return mongodbURI;
	}

	@Override
	public void loadAllNewFilesToMongoByFolder(String folder) throws Exception {
		// TODO Auto-generated method stub
		try {
			GcpService gcpService = new GcpService();
			Set<String> subfolders = gcpService.getAllGCSFoldersByParentFolder(folder);

			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
			String datetime = simpleDateFormat.format(new Date());
			Date now = java.util.Date
					.from(ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC-4")).toInstant());

			if (subfolders.size() > 0) {
				ExecutorService taskExecutor = Executors.newFixedThreadPool(subfolders.size());

				for (String subfolder : subfolders) {
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							//MongoClient mongo = null;
							try {
								//mongo = MongoClients.create(mongodbURI);
								//List<GcpFile> files = gcpService.getAllGCSFileByFolder(subfolder,);
								
								gcpService.getAllGCSFileByFolder(subfolder,mongoClient.getDatabase(mongodbDatabase));
								/*
								 * List<JSONObject> entities = new ArrayList<JSONObject>();
								 * 
								 * for (GcpFile file : files) { JSONObject entity = new JSONObject();
								 * entity.put("FileName", file.getFileNameWithoutPath());
								 * entity.put("FileContent", gcpService.readGcsFile(file));
								 * 
								 * Document doc = new Document();
								 * 
								 * switch (file.getFileExt().toUpperCase()) { case "JSON": doc =
								 * Document.parse(file.getContent()); break; case "XML": JSONObject xmlJSONObj =
								 * XML.toJSONObject(file.getContent()); String jsonPrettyPrintString =
								 * xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR); doc =
								 * Document.parse(jsonPrettyPrintString); break; case "CSV": doc =
								 * Document.parse(parseCSVFile(file)); break; default: doc.put("File_Content",
								 * file.getContent()); break; }
								 * 
								 * doc.put("_id", file.getParentFolder() + "-" + file.getFileNameWithoutPath() +
								 * "-" + datetime); doc.put("Name", file.getFileNameWithoutPath());
								 * doc.put("Loading_Time", now);
								 * 
								 * LoggingUtilities.generateInfoLog("Loading File " +
								 * file.getFileNameWithoutPath() + " from " + file.getPath());
								 * mongo.getDatabase(mongodbDatabase).getCollection(file.getParentFolder())
								 * .insertOne(doc); LoggingUtilities.generateInfoLog( "File " +
								 * file.getFileNameWithoutPath() + " is loaded successfully. ");
								 * 
								 * gcpService.moveFile(file, file.getPath() + "/backup");
								 * 
								 * entities.add(entity); }
								 */
								// mongo.close();
							} catch (Exception ex) {
								LoggingUtilities.generateErrorLog(ex.getMessage());
								ex.printStackTrace();
							} finally {
								/*
								 * if (mongo != null) mongo.close();
								 */
							}
						}
					});
				}

				taskExecutor.shutdown();
				taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
	}

	@Override
	public void loadFilesToMongoByFolderIgnoreSubFolders(String folder) throws Exception {
		// TODO Auto-generated method stub
		try {
			GcpService gcpService = new GcpService();

			List<GcpFile> files = gcpService.getAllGCSFileByFolder(folder);
			List<JSONObject> entities = new ArrayList<JSONObject>();

			for (GcpFile file : files) {
				JSONObject entity = new JSONObject();
				entity.put("FileName", file.getFileName());
				entity.put("FileContent", gcpService.readGcsFile(file));

				LoggingUtilities
						.generateInfoLog("Loading File " + file.getFileNameWithoutPath() + " from " + file.getPath());

				switch (file.getFileExt()) {
				case "json":
					this.loadJSonFileToMongo(file);
					break;
				case "xml":
					loadXMLFileToMongo(file);
					break;
				case "csv":
					loadCSVFileToMongo(file);
					break;
				default:
					;
				}

				loadFileToMongo(file);
				LoggingUtilities.generateInfoLog("File " + file.getFileNameWithoutPath() + " is loaded successfully. ");

				gcpService.moveFile(file, folder + "/backup");

				entities.add(entity);
			}
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
	}

	private void loadFileToMongo(GcpFile file) throws Exception {
		// TODO Auto-generated method stub
		try {
			switch (file.getFileExt()) {
			case "json":
				this.loadJSonFileToMongo(file);
				break;
			case "xml":
				loadXMLFileToMongo(file);
				break;
			case "csv":
				loadCSVFileToMongo(file);
				break;
			default:
				;
			}
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
	}

	private String parseCSVFile(GcpFile file) throws Exception {
		String result = "";
		try {
			InputStream in = new ByteArrayInputStream(file.getContent().getBytes());

			CSVReader csv = new CSVReader(true, ',', in);
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

			result = jsonString.substring(1, jsonString.length() - 1);
		} catch (Exception ex) {
			throw ex;
		}

		return result;
	}

	private void loadCSVFileToMongo(GcpFile file) throws Exception {
		try {
			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

			String datetime = simpleDateFormat.format(new Date());

			InputStream in = new ByteArrayInputStream(file.getContent().getBytes());

			CSVReader csv = new CSVReader(true, ',', in);
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
			doc.put("_id", file.getParentFolder() + "-" + file.getFileNameWithoutPath() + "-" + datetime);
			doc.put("Name", file.getFileNameWithoutPath());

			Date date = new Date();
			SimpleDateFormat DATETIMEFORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
			doc.put("Loading_Time",
					java.util.Date.from(
							ZonedDateTime.ofInstant(DATETIMEFORMATTER.parse(DATETIMEFORMATTER.format(date)).toInstant(),
									ZoneId.of("Canada/Eastern")).toInstant()));

			mongoClient.getDatabase(mongodbDatabase).getCollection(file.getParentFolder()).insertOne(doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private void loadJSonFileToMongo(GcpFile file) throws Exception {
		try {
			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

			String datetime = simpleDateFormat.format(new Date());

			Document doc = Document.parse(file.getContent());
			doc.put("_id", file.getParentFolder() + "-" + file.getFileNameWithoutPath() + "-" + datetime);
			doc.put("Name", file.getFileNameWithoutPath());
			doc.put("Loading_Time", LocalDateTime.now());
			mongoClient.getDatabase(mongodbDatabase).getCollection(file.getParentFolder()).insertOne(doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	private void loadXMLFileToMongo(GcpFile file) throws Exception {
		try {
			String datePattern = "yyyyMMddHHmmssSSS";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

			String datetime = simpleDateFormat.format(new Date());

			JSONObject xmlJSONObj = XML.toJSONObject(file.getContent());
			String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

			Document doc = Document.parse(jsonPrettyPrintString);
			doc.put("_id", file.getParentFolder() + "-" + file.getFileNameWithoutPath() + "-" + datetime);
			doc.put("Name", file.getFileNameWithoutPath());
			doc.put("Loading_Time", LocalDateTime.now());
			mongoClient.getDatabase(mongodbDatabase).getCollection(file.getParentFolder()).insertOne(doc);
		} catch (Exception ex) {
			throw ex;
		}
	}

	public void setMongodbDatabase(String mongodbDatabase) {
		this.mongodbDatabase = mongodbDatabase;
	}

	public void setMongodbURI(String mongodbURI) {
		this.mongodbURI = mongodbURI;
	}

}
