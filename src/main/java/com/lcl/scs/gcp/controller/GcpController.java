package com.lcl.scs.gcp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lcl.scs.response.ListData;
import com.lcl.scs.util.logging.LoggingUtilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcl.scs.gcp.model.GcpFile;
import com.lcl.scs.gcp.service.impl.GcpService;
import com.lcl.scs.interfaces.service.impl.LoadFileToMongoServiceImpl;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/gcp")
public class GcpController {
	@Value("${spring.data.mongodb.uri}")
	private String mongodbURI;
	@Value("${spring.data.mongodb.database}")
	private String mongodbDatabase;

	@PostMapping(value = "/readeachfile", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> getGCPFile(@RequestHeader("foldername") String foldername) {
		try {
			GcpService gcpService = new GcpService();

			List<GcpFile> files = gcpService.getAllGCSFileByFolder(foldername);

			List<JSONObject> entities = new ArrayList<JSONObject>();

			for (GcpFile file : files) {
				JSONObject entity = new JSONObject();
				entity.put("FileName", file.getFileName());
				entity.put("FileContent", gcpService.readGcsFile(file));

				entities.add(entity);
			}
			return new ResponseEntity<Object>(entities, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/listfiles", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> listGCPFile(@RequestHeader("foldername") String foldername) {
		try {
			GcpService gcpService = new GcpService();

			List<GcpFile> files = gcpService.getAllGCSFileByFolder(foldername);

			List<JSONObject> entities = new ArrayList<JSONObject>();
			for (GcpFile file : files) {
				JSONObject entity = new JSONObject();
				entity.put("FileName", file.getFileName());
				entities.add(entity);
			}
			return new ResponseEntity<Object>(entities, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/loadgcpfiletomongobyfolderignoresubfolders", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> loadGCPFilesToMongoByFolderIgnoreSubFolders(
			@RequestHeader("foldername") String foldername) {
		try {
			LoadFileToMongoServiceImpl loadFileToMongo = new LoadFileToMongoServiceImpl();
			loadFileToMongo.setMongodbDatabase(mongodbDatabase);
			loadFileToMongo.setMongodbURI(mongodbURI);
			loadFileToMongo.loadFilesToMongoByFolderIgnoreSubFolders(foldername);
			loadFileToMongo.destory();
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/loadallnewfilestomongo", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Object> loadAllNewGCPFilesToMongo() {
		try {
			LoggingUtilities.generateErrorLog("MongoDB URI: " + mongodbURI);
			LoadFileToMongoServiceImpl loadFileToMongo = new LoadFileToMongoServiceImpl();
			loadFileToMongo.setMongodbDatabase(mongodbDatabase);
			loadFileToMongo.setMongodbURI(mongodbURI);
			loadFileToMongo.loadAllNewFilesToMongoByFolder("");
			loadFileToMongo.destory();
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog(ex.getMessage());
			ex.printStackTrace();
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
