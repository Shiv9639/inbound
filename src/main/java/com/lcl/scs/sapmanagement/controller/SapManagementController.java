package com.lcl.scs.sapmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcl.scs.sapmanagement.constants.SapManagementConstants;
import com.lcl.scs.sapmanagement.service.SapManagementService;
import com.lcl.scs.sapmanagement.util.ValidationUtil;
import com.lcl.scs.util.logging.LoggingUtilities;


@RestController
@RequestMapping("/scs/sap-management")
public class SapManagementController {

	@Autowired
	private SapManagementService sapManagementService;

	@PostMapping(value = { "/xml-message" }, consumes = { MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public String pushXMLMessage(@RequestHeader("interfaceId") String interfaceId,
			@RequestHeader("fileName") String fileName, @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
			@RequestBody String messageBody) {

		LoggingUtilities.generateInfoLog("Recieving request to save xml data with interfaceId : "+ interfaceId+"::contentType:"+contentType+" and fileName: "+fileName);
		
        String status = ValidationUtil.validateXmlHeaders(interfaceId, fileName);

		
		/*HttpStatus statusCode = HttpStatus.OK;

		if (!(status.equals("Success"))) {
			statusCode = HttpStatus.BAD_REQUEST;
		} else {*/
			status = sapManagementService.processMessage(interfaceId, fileName, messageBody,contentType);

		//}
		return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
	}

	@PostMapping(value = { "/flat-message" }, consumes = { MediaType.TEXT_PLAIN_VALUE })
	public String pushFlatMessage(@RequestHeader("interfaceId") String interfaceId,
			@RequestHeader("fileName") String fileName, @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
			@RequestBody String messageBody) {

		LoggingUtilities.generateInfoLog("Recieving request to save text data with interfaceId : "+ interfaceId+"::contentType:"+contentType+" and fileName: "+fileName);

		String status = ValidationUtil.validateFlatHeaders(interfaceId, fileName);

		//HttpStatus statusCode = HttpStatus.OK;

		
			status = sapManagementService.processMessage(interfaceId, fileName, messageBody,contentType);

		
		return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
	}

}
