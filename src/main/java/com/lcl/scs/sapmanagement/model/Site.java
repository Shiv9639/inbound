package com.lcl.scs.sapmanagement.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lcl.scs.sapmanagement.constants.SapManagementConstants;

@Document(SapManagementConstants.SITE_DEST_COLL)
public class Site {
	@Id
	private String location;
	private String region;
	private Date dateUpdated;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	

}
