package com.lcl.scs.sapmanagement.service;

public interface SapManagementService {
	public String processMessage(String interfaceId, String fileName, String xmlMessage, String contentType);
	/*
	 * public String processXMLMessage(String interfaceId, String fileName, String
	 * xmlMessage, String contentType); public String processFlatMessage(String
	 * interfaceId, String fileName, String payload,String contentType) ;
	 */
}
