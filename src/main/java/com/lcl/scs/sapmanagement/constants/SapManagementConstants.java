package com.lcl.scs.sapmanagement.constants;

/**
 * 
 * @author rajesh.l.singh
 * 
 * Returns Constants used across API
 *
 */
public interface SapManagementConstants {

	String SITE_INTERFACE_ID = "R10039";
	String SITE_INTERFACE_DESC = "SITE_INTERFACE";
	String SITE_DEST_COLL = "Site";

	String ARTICLE_INTERFACE_ID = "R10041";
	String ARTICLE_INTERFACE_DESC = "ARTICLE_INTERFACE";
	String ARTICLE_DEST_COLL = "ArticleMaster";

	String SITEXREF_INTERFACE_ID = "R10040";
	String SITEXREF_INTERFACE_DESC = "SITEXREF_INTERFACE";
	String SITEXREF_DEST_COLL = "StoreMaster";
	
	String CUSTOMER_DELTA_INTERFACE_ID="R10175";
	String CUSTOMER_DELTA_INTERFACE_DESC = "CUSTOMER_DELTA_INTERFACE";
	String CUSTOMER_DELTA_INTERFACE_COLL = "CustomerDeltaMaster";
	
	
	String CUSTOMER_INTERFACE_ID = "R10045";
	String CUSTOMER_INTERFACE_DESC = "CUSTOMER_INTERFACE";
	String CUSTOMER_DEST_COLL = "CustomerMaster";
	
	String VENDOR_INTERFACE_ID = "R10052";
	String VENDOR_INTERFACE_DESC = "VENDOR_INTERFACE";
	String VENDOR_DEST_COLL = "VendorMaster";

	
	String FILE_ERR_MSG = "Missing File Name";

	String XREFINTERFACE_ERR_MSG = "Invalid Interface ID";
	
//	String SITEINTERFACE_ERR_MSG = "Invalid Interface ID for SITE";
	String INTERFACE_ERR_MSG ="Invalid Interface ID";
	String INTERFACE_EMPTY_MSG = "Missing Interface ID";

	String PAYLOAD_SUCCESS_MSG = "Payload Successfully Loaded";

}
