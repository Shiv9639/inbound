package com.lcl.scs.interfaces.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "interfaces")
@ToString
public class Interfaces {
	@Id
	@Getter
	@Setter
	private String id;
	@Getter
	@Setter
	private String ricefnum;
	@Getter
	@Setter
	private String source;
	@Getter
	@Setter
	private String encodingType;
	
	public String getEncodingType() {
		return encodingType;
	}
	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}
	public String getRicefnum() {
		return ricefnum;
	}
	public void setRicefnum(String ricefnum) {
		this.ricefnum = ricefnum;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
