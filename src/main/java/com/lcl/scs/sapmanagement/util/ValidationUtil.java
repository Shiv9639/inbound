package com.lcl.scs.sapmanagement.util;

import com.lcl.scs.exception.BadRequestException;
import com.lcl.scs.sapmanagement.constants.SapManagementConstants;

public class ValidationUtil {

	public static String validateFlatHeaders(String interfaceId, String fileName) {
		if (interfaceId == null || interfaceId.isEmpty()) {
			throw new BadRequestException(SapManagementConstants.INTERFACE_EMPTY_MSG);
		}

		if (interfaceId.equalsIgnoreCase(SapManagementConstants.SITEXREF_INTERFACE_ID) 
				|| SapManagementConstants.ARTICLE_INTERFACE_ID.equalsIgnoreCase(interfaceId) 
				|| SapManagementConstants.VENDOR_INTERFACE_ID.equalsIgnoreCase(interfaceId)
				|| SapManagementConstants.CUSTOMER_INTERFACE_ID.equalsIgnoreCase(interfaceId)) {
			return validateFile(fileName);
		} else {
			throw new BadRequestException(SapManagementConstants.XREFINTERFACE_ERR_MSG);
			// return SapManagementConstants.XREFINTERFACE_ERR_MSG;
		}

	}

	public static String validateXmlHeaders(String interfaceId, String fileName) {
		if (interfaceId == null || interfaceId.isEmpty()) {
			throw new BadRequestException(SapManagementConstants.INTERFACE_EMPTY_MSG);
		}
		else if (interfaceId.equalsIgnoreCase(SapManagementConstants.SITE_INTERFACE_ID)) {
			return validateFile(fileName);
		}
		else if (interfaceId.equalsIgnoreCase(SapManagementConstants.VENDOR_INTERFACE_ID)) {
			return validateFile(fileName);
		} 
		else if (interfaceId.equalsIgnoreCase(SapManagementConstants.CUSTOMER_DELTA_INTERFACE_ID)) {
			return validateFile(fileName);
		}
		else
		{
		throw new BadRequestException(SapManagementConstants.INTERFACE_ERR_MSG);
		}

	}

	private static String validateFile(String fileName) {
		if(fileName ==null || fileName.isEmpty()) {
			throw new BadRequestException(SapManagementConstants.FILE_ERR_MSG);
		}
		return "Success";
		/*if (fileName != null && !fileName.trim().isEmpty()) {
			return "Success";
		} else {
			return SapManagementConstants.FILE_ERR_MSG;
		}*/
	}

}
