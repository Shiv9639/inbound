package com.lcl.scs.interfaces.service;

import com.lcl.scs.gcp.model.GcpFile;
import com.lcl.scs.gcp.service.impl.GcpService;

public interface LoadFileToMongoService {
//	public void loadJSonFileToMongo(String fileName) throws Exception;
//	public void loadXMLFileToMongo(String fileName) throws Exception;
	public void destory() throws Exception;
	public void loadAllNewFilesToMongoByFolder(String folder) throws Exception;
	public void loadFilesToMongoByFolderIgnoreSubFolders(String folder) throws Exception;
}
