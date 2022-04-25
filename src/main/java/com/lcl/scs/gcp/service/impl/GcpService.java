package com.lcl.scs.gcp.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.bson.Document;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lcl.scs.dallas.controller.DallasController;
import com.lcl.scs.dallas.dao.DallasDetailJdbcDao;
import com.lcl.scs.dallas.dao.DallasHeaderJdbcDao;
import com.lcl.scs.dallas.dao.DetailDao;
import com.lcl.scs.dallas.dao.HeaderDao;
import com.lcl.scs.dallas.model.*;
import com.google.api.gax.paging.Page;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;
import com.lcl.scs.gcp.model.GcpFile;
import com.lcl.scs.util.CSVReader;
import com.lcl.scs.util.logging.LoggingUtilities;
import com.mongodb.client.MongoDatabase;


public class GcpService {
	private static int PRETTY_PRINT_INDENT_FACTOR = 4;
	

	private Bucket bucket;
	private String bucketURI = System.getenv("GCP_BUCKET_NAME");
	private String gcpCredentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
	private String gcpProjectId = System.getenv("GCP_PROJECT_ID");
	private Storage storage;

	public GcpService() throws Exception {
		super();
		try {
			Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(gcpCredentialsPath));
			storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(gcpProjectId).build()
					.getService();
			
//			LoggingUtilities.generateInfoLog("GOOGLE_APPLICATION_CREDENTIALS: "+System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
//			storage = StorageOptions.getDefaultInstance().getService();

			bucket = storage.get(bucketURI);
			

		} catch (Exception ex) {
			throw ex;
		}
	}

	public List<GcpFile> getAllGCSFileByFolder(String folderName) throws Exception {

		List<GcpFile> result = new ArrayList<GcpFile>();

		Set<String> folders = new HashSet<String>();

		Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(folderName),
				Storage.BlobListOption.currentDirectory());
		for (Blob blob : blobs.iterateAll()) {
			// Gets the path of the object
			String path = blob.getName();

			
			if (isDir(path) && !folders.contains(path)) { // If the object is a folder, then add it to folders set
				folders.add(path);
			} else { // If the object isn't a folder, then extract the parent path and add it to
						// folders set
				GcpFile file = new GcpFile();
				file.setFileName(blob.getName());
				file.setBlobId(blob.getBlobId());
				if (file.getFileName() !=null && !file.getFileName().isEmpty() && file.getFileName().endsWith(".Z")) {
					try {
					BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(blob.getContent()));
					ZCompressorInputStream zIn = new ZCompressorInputStream(
							new ByteArrayInputStream(blob.getContent()));
					final byte[] buffer = new byte[4096];
					int n = 0;
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while (-1 != (n = zIn.read(buffer))) {
						out.write(buffer, 0, n);
					}
					file.setContent(new String(out.toByteArray()));
					in.close();
					zIn.close();
					out.close();
					}catch(Exception e) {
						LoggingUtilities.generateErrorLog("Error Processing the file:"+blob.getName()+": error:"+e.getMessage());
						file.setFileNameWithoutPath(file.getFileName().substring(file.getFileName().lastIndexOf("/") + 1));
						file.setPath(file.getFileName().substring(0, file.getFileName().lastIndexOf("/")));
						moveFile(file, file.getPath() + "/error");
						continue;
					}
				} else {
					file.setContent(new String(blob.getContent()));
				}
				file.setFileNameWithoutPath(file.getFileName().substring(file.getFileName().lastIndexOf("/") + 1));
				file.setPath(file.getFileName().substring(0, file.getFileName().lastIndexOf("/")));
				file.setFileExt(
						file.getFileNameWithoutPath().substring(file.getFileNameWithoutPath().lastIndexOf(".") + 1));
				file.setParentFolder(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));
				result.add(file);
			}
		}

		return result;
	}
	public void getAllGCSFileByFolder(String folderName,MongoDatabase mongodb) throws Exception {

		

		//List<GcpFile> result = new ArrayList<GcpFile>();
		String datePattern = "yyyyMMddHHmmssSSS";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
		String datetime = simpleDateFormat.format(new Date());
		Date now = java.util.Date
				.from(ZonedDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC-4")).toInstant());

		Set<String> folders = new HashSet<String>();

		Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(folderName),
				Storage.BlobListOption.currentDirectory());
		if(blobs ==null) {
			return;
		}
		for (Blob blob : blobs.iterateAll()) {
			if(blob ==null) {
				continue;
			}
			// Gets the path of the object
			String path = blob.getName();

			
			if (isDir(path) && !folders.contains(path)) { // If the object is a folder, then add it to folders set
				folders.add(path);
			} else { // If the object isn't a folder, then extract the parent path and add it to
						// folders set
				GcpFile file = new GcpFile();
				file.setFileName(blob.getName());
				file.setBlobId(blob.getBlobId());
				if (file.getFileName() !=null && !file.getFileName().isEmpty() && file.getFileName().endsWith(".Z")) {
					try {
					BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(blob.getContent()));
					ZCompressorInputStream zIn = new ZCompressorInputStream(
							new ByteArrayInputStream(blob.getContent()));
					final byte[] buffer = new byte[4096];
					int n = 0;
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					while (-1 != (n = zIn.read(buffer))) {
						out.write(buffer, 0, n);
					}
					file.setContent(new String(out.toByteArray()));
					in.close();
					zIn.close();
					out.close();
					}catch(Exception e) {
						LoggingUtilities.generateErrorLog("Error Processing the file:"+blob.getName()+": error:"+e.getMessage());
						file.setFileNameWithoutPath(file.getFileName().substring(file.getFileName().lastIndexOf("/") + 1));
						file.setPath(file.getFileName().substring(0, file.getFileName().lastIndexOf("/")));
						moveFile(file,  "error"+file.getPath());
						continue;
					}
				} else {
					file.setContent(new String(blob.getContent()));
				}
				file.setFileNameWithoutPath(file.getFileName().substring(file.getFileName().lastIndexOf("/") + 1));
				file.setPath(file.getFileName().substring(0, file.getFileName().lastIndexOf("/")));
				file.setFileExt(
						file.getFileNameWithoutPath().substring(file.getFileNameWithoutPath().lastIndexOf(".") + 1));
				file.setParentFolder(file.getPath().substring(file.getPath().lastIndexOf("/") + 1));
				//result.add(file);
				/*
				 * JSONObject entity = new JSONObject(); entity.put("FileName",
				 * file.getFileNameWithoutPath()); String readGscFile = readGcsFile(file);
				 * if(readGscFile ==null || readGscFile.isEmpty()) { continue; }
				 * entity.put("FileContent", readGscFile);
				 */

				Document doc = new Document();
				
				switch (file.getFileExt().toUpperCase()) {
				case "JSON":
					doc = Document.parse(file.getContent());
					break;
				case "XML":
					JSONObject xmlJSONObj = XML.toJSONObject(file.getContent());
					String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
					doc = Document.parse(jsonPrettyPrintString);
					break;
				case "CSV":
					
					try {
						if(file.getPath().contains("R10095")) {
							String fileContents = readGcsFile(file);
							DallasController dallasController = new DallasController();
							if(file.getFileNameWithoutPath().contains("HDR")) {
								List<DallasHeader> header = dallasController.readHeaderCSV(fileContents, file.getFileNameWithoutPath());
								
								if(file.getFileNameWithoutPath().contains("DC30")) {
									dallasController.insertHeaderToODS(header, 100);
								}else if(file.getFileNameWithoutPath().contains("DC50")) {
									dallasController.insertD50HeaderToODS(header, 100);
								}		
							}else if(file.getFileNameWithoutPath().contains("DTL")) {
								List<DallasDetail> details = dallasController.readDetailCSV(fileContents, file.getFileNameWithoutPath());
								if(file.getFileNameWithoutPath().contains("DC30")) {
									dallasController.insertDetailToODS(details, 100);
								}else if(file.getFileNameWithoutPath().contains("DC50")) {
									dallasController.insertD50DetailToODS(details, 100);
								}
							}
							
							break;
						}else {
							doc = Document.parse(parseCSVFile(file));
							break;
						}
					
					} catch (Exception e) {
						LoggingUtilities.generateErrorLog("Failed to load all new file: " + file.getFileNameWithoutPath()+ e.getMessage());
						e.printStackTrace();
						// TODO Auto-generated catch block
						moveFailedFile(file, file.getPath() + "/backup");
						throw(e);
					}
					
					//doc = Document.parse(parseCSVFile(file));
					//break;
				default:
					doc.put("File_Content", file.getContent());
					break;
				}

				doc.put("_id", file.getParentFolder() + "-" + file.getFileNameWithoutPath() + "-"
						+ datetime);
				doc.put("Name", file.getFileNameWithoutPath());
				doc.put("Loading_Time", now);

				LoggingUtilities.generateInfoLog("Loading File " + file.getFileNameWithoutPath()
						+ " from " + file.getPath());
				
				mongodb.getCollection(file.getParentFolder())
						.insertOne(doc);
				//System.out.println("After Mongo");
				LoggingUtilities.generateInfoLog(
						"File " + file.getFileNameWithoutPath() + " is loaded successfully. ");

				moveFile(file, file.getPath() + "/backup");
			}
		}

		
	}


	

	public Set<String> getAllGCSFoldersByParentFolder(String folderName) throws Exception {
		Set<String> folders = new HashSet<String>();
		try {
			Page<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(folderName),
					Storage.BlobListOption.currentDirectory());
			for (Blob blob : blobs.iterateAll()) {
				// Gets the path of the object
				String path = blob.getName();

				if (isDir(path) && !folders.contains(path)) { // If the object is a folder, then add it to folders set
					folders.add(path);
				}
			}
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
		return folders;
	}

	public String getParentDir(String path) {
		return path.substring(0, path.lastIndexOf("/") + 1);
	}

	public boolean isDir(String path) {
		return path.endsWith("/");
	}

	public void copyFile(GcpFile file, String targetFolder) throws Exception {
		try {
			Blob blob = storage.get(file.getBlobId());
			blob.copyTo(BlobId.of(bucket.getName(), targetFolder + "/" + file.getFileNameWithoutPath()));
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			throw ex;
		}
	}

	public void moveFile(GcpFile file, String targetFolder) throws Exception {
		try {
			Blob blob = storage.get(file.getBlobId());
			blob.copyTo(BlobId.of(bucket.getName(), targetFolder + "/" + file.getFileNameWithoutPath()));
			blob.delete();
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			//throw ex;
		}
	}
	public void moveFailedFile(GcpFile file, String targetFolder) throws Exception {
		try {
			Blob blob = storage.get(file.getBlobId());
			blob.copyTo(BlobId.of(bucket.getName(), targetFolder + "/" + "failed_" + file.getFileNameWithoutPath()));
			blob.delete();
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			//throw ex;
		}
	}

	public String readGcsFile(GcpFile file) throws Exception {
		String content = "";
		try {
			Blob blob = storage.get(file.getBlobId());
			if(blob ==null) {
				return null;
			}
			content = new String(blob.getContent());
		} catch (Exception ex) {
			throw ex;
		}

		return content;
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


}
