package com.lcl.scs.gcp.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lcl.scs.gcp.service.impl.SubscribersService;
import com.lcl.scs.interfaces.service.impl.LoadFileToMongoServiceImpl;
import com.lcl.scs.subscribers.model.Subscribers;
import com.lcl.scs.util.logging.LoggingUtilities;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Component
public class LoadAllNewFilesScheduler {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	@Value("${spring.data.mongodb.uri}")
	private String mongodbURI;
	@Value("${spring.data.mongodb.database}")
	private String mongodbDatabase;
	

	@Autowired
	private SubscribersService subscribersService;
	

	@Scheduled(fixedDelay = 1000 * 60 * 5)
	@SchedulerLock(name = "LoadAllNewFilesScheduler", lockAtLeastFor = "PT5M", lockAtMostFor = "PT10M")
	public void loadAllNewFiles() {
		LoggingUtilities.generateInfoLog(dateFormat.format(new Date()) + ": Start loading all new files");

		try {
			Subscribers subscribers = subscribersService.findByName("GCS_LOAD");

		
			
			if (subscribers == null) {
				subscribers = new Subscribers();
				subscribers.setName("GCS_LOAD");
				subscribers.setCreatedby("SCS_INBOUND_SERVICE");
				subscribers.setCreateddate(new Date());
				subscribers.setLastrun(null);
				subscribers.setStatus("Running");
				subscribers.setCoolDownTimeInMinutes(30);
				subscribersService.save(subscribers);
			} else {
				String status = subscribers.getStatus();
				int coolDownTimeInM = subscribers.getCoolDownTimeInMinutes();
				
				if(coolDownTimeInM ==0) {
					coolDownTimeInM=30;
					LoggingUtilities.generateInfoLog("setting cooldown time:"+coolDownTimeInM);
					subscribers.setCoolDownTimeInMinutes(30);
					
				}
				LoggingUtilities.generateInfoLog("CoolDOwnTimeInMinutes:"+coolDownTimeInM);
				if ("Running".equalsIgnoreCase(status)) {
					long t = System.currentTimeMillis();
					long lastT = subscribers.getLastrun().getTime();
					
					long diff = (t-lastT)/1000;
					LoggingUtilities.generateInfoLog("LastRun Diff:"+diff);
					
					if(diff >(coolDownTimeInM*60)) {
						LoggingUtilities.generateInfoLog("GCS_LOAD SCheduler reset status");
						subscribers.setStatus("Stopped");
						subscribersService.save(subscribers);
					}
					LoggingUtilities.generateInfoLog("GCS_LOAD SCheduler already running");
					return;
					
				} else {
					subscribers.setStatus("Running");
					subscribersService.save(subscribers);
				}
			}
			// mongodbURI=System.getenv("MongoDBURI");
			// mongodbDatabase=System.getenv("MongoDBDatabaseName");
			LoggingUtilities.generateInfoLog("MongoDB URI: " + mongodbURI);
			LoggingUtilities.generateInfoLog("MongoDB Database Name: " + mongodbDatabase);

			LoadFileToMongoServiceImpl loadFileToMongo = new LoadFileToMongoServiceImpl();
			loadFileToMongo.createClient(mongodbURI, mongodbDatabase);
			/*
			 * loadFileToMongo.setMongodbDatabase(mongodbDatabase);
			 * loadFileToMongo.setMongodbURI(mongodbURI);
			 */
			loadFileToMongo.loadAllNewFilesToMongoByFolder("incoming/");
			loadFileToMongo.destory();
			subscribers.setLastrun(new Date());
			subscribers.setStatus("Stopped");
			subscribersService.save(subscribers);
		} catch (Exception ex) {
			LoggingUtilities.generateErrorLog("Failed to load all new files! " + ex.getMessage());
			ex.printStackTrace();
		}
		LoggingUtilities.generateInfoLog(dateFormat.format(new Date()) + ": Finish loading all new files");
	}
}
