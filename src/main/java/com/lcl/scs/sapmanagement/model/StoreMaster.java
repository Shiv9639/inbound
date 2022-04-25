package com.lcl.scs.sapmanagement.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.lcl.scs.sapmanagement.constants.SapManagementConstants;


@Document(SapManagementConstants.SITEXREF_DEST_COLL)
public class StoreMaster {
	
	@Id
	private StoreMasterId id;
	/*
	 * private String externalLocationNumber;
	 * 
	 * private String internalLocationNumber;
	 */

	private String mappingType;
	
	private String openIndicator;
	
	private String extractDateTime;
	
	private String internalLocationType;
	
	private String externalLocationType;
	
	private String externalGroup;
	
	private String activeDate;
	
	private String openDate;
	
	private String closeDate;
	
	private String deactiveDate;
	
	private String operationDateTime;
	
	private String activeIndicator;
	
	private String creationUserId;
	
	private String creationDateTime;
	
	private String lastChangeUserId;
	
	private String lastChangeDateTime;
	
	private String flowType;
	
	private String flowValidFrom;
	
	private String flowChangeUserId;
	
	private String flowChangeDateTime;
	private String soleProprietor;
	
	@DateTimeFormat(iso=ISO.DATE_TIME)
	@CreatedDate
	private LocalDateTime createdDate;
	
	private String createdBy;
	
	@DateTimeFormat(iso=ISO.DATE_TIME)
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	private String updatedBy;

	
	

	public StoreMasterId getId() {
		return id;
	}

	public void setId(StoreMasterId id) {
		this.id = id;
	}

	/*
	 * public String getExternalLocationNumber() { return externalLocationNumber; }
	 * 
	 * public void setExternalLocationNumber(String externalLocationNumber) {
	 * this.externalLocationNumber = externalLocationNumber; }
	 * 
	 * public String getInternalLocationNumber() { return internalLocationNumber; }
	 * 
	 * public void setInternalLocationNumber(String internalLocationNumber) {
	 * this.internalLocationNumber = internalLocationNumber; }
	 */
	public String getMappingType() {
		return mappingType;
	}

	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}

	public String getOpenIndicator() {
		return openIndicator;
	}

	public void setOpenIndicator(String openIndicator) {
		this.openIndicator = openIndicator;
	}

	public String getExtractDateTime() {
		return extractDateTime;
	}

	public void setExtractDateTime(String extractDateTime) {
		this.extractDateTime = extractDateTime;
	}

	public String getInternalLocationType() {
		return internalLocationType;
	}

	public void setInternalLocationType(String internalLocationType) {
		this.internalLocationType = internalLocationType;
	}

	public String getExternalLocationType() {
		return externalLocationType;
	}

	public void setExternalLocationType(String externalLocationType) {
		this.externalLocationType = externalLocationType;
	}

	public String getExternalGroup() {
		return externalGroup;
	}

	public void setExternalGroup(String externalGroup) {
		this.externalGroup = externalGroup;
	}

	public String getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getDeactiveDate() {
		return deactiveDate;
	}

	public void setDeactiveDate(String deactiveDate) {
		this.deactiveDate = deactiveDate;
	}

	public String getOperationDateTime() {
		return operationDateTime;
	}

	public void setOperationDateTime(String operationDateTime) {
		this.operationDateTime = operationDateTime;
	}

	public String getActiveIndicator() {
		return activeIndicator;
	}

	public void setActiveIndicator(String activeIndicator) {
		this.activeIndicator = activeIndicator;
	}

	public String getCreationUserId() {
		return creationUserId;
	}

	public void setCreationUserId(String creationUserId) {
		this.creationUserId = creationUserId;
	}

	public String getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(String creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getLastChangeUserId() {
		return lastChangeUserId;
	}

	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

	public String getLastChangeDateTime() {
		return lastChangeDateTime;
	}

	public void setLastChangeDateTime(String lastChangeDateTime) {
		this.lastChangeDateTime = lastChangeDateTime;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public String getFlowValidFrom() {
		return flowValidFrom;
	}

	public void setFlowValidFrom(String flowValidFrom) {
		this.flowValidFrom = flowValidFrom;
	}

	public String getFlowChangeUserId() {
		return flowChangeUserId;
	}

	public void setFlowChangeUserId(String flowChangeUserId) {
		this.flowChangeUserId = flowChangeUserId;
	}

	public String getFlowChangeDateTime() {
		return flowChangeDateTime;
	}

	public void setFlowChangeDateTime(String flowChangeDateTime) {
		this.flowChangeDateTime = flowChangeDateTime;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getSoleProprietor() {
		return soleProprietor;
	}

	public void setSoleProprietor(String soleProprietor) {
		this.soleProprietor = soleProprietor;
	}
	

}
