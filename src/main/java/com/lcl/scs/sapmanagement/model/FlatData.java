package com.lcl.scs.sapmanagement.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


public class FlatData{
	
	@Id
	private String id;
	
	//private String interfaceId;
	private String fileName;
	private String interfaceDesc;
	private String destCollection;

	private String content;
	private String contentType;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*
	 * public String getInterfaceId() { return interfaceId; }
	 * 
	 * public void setInterfaceId(String interfaceId) { this.interfaceId =
	 * interfaceId; }
	 */
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getInterfaceDesc() {
		return interfaceDesc;
	}

	public void setInterfaceDesc(String interfaceDesc) {
		this.interfaceDesc = interfaceDesc;
	}

	public String getDestCollection() {
		return destCollection;
	}

	public void setDestCollection(String destCollection) {
		this.destCollection = destCollection;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@DateTimeFormat(iso=ISO.DATE_TIME)
	@CreatedDate
	private LocalDateTime createdDate = LocalDateTime.now();
	
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


}
