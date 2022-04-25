package com.lcl.scs.sapmanagement.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lcl.scs.sapmanagement.constants.SapManagementConstants;

@Document(SapManagementConstants.ARTICLE_DEST_COLL)
public class ArticleMaster {
	@Id
	private String id;
	private Date articleUpdatedTime;
	private Date articleProcessedTime;
	private String articleNumber;
	private String articleDescription;
	private String materialType;
	private String materialGroup;
	private String baseUOM;
	private String brandId;
	private String exMerchandiseCat;
	private String materialStatus;
	private String validFrom;
	private String createdOn;
	private String tempCondIndicator;
	private String zzszqty;
	private String sizeUOM;
	private String c;
	private String gtin;
	private String gtinCategory;
	private String consecutiveNumber;
	private String indicator;
	private String length;
	private String width;
	private String height;
	private String dimensionUnit;
	private String weightUnit;
	private String grossWeight;
	private String salesOrg;
	private String distributionChannel;
	private String country;
	private String articleUpdated;
	private double value;
	private String LOB="LOBLAW";
	private String participant="Loblaw";
	private String itemType="Sold";
	private String itemowner="Loblaw";
	private String processIndicator;
	private String articleFileName;
	private Date creationDate;
	
	public Date getArticleProcessedTime() {
		return articleProcessedTime;
	}
	public void setArticleProcessedTime(Date articleProcessedTime) {
		this.articleProcessedTime = articleProcessedTime;
	}
	public String getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(String articleNumber) {
		this.articleNumber = articleNumber;
	}
	public String getArticleDescription() {
		return articleDescription;
	}
	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	public String getMaterialType() {
		return materialType;
	}
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}	
	public String getMaterialGroup() {
		return materialGroup;
	}
	public void setMaterialGroup(String materialGroup) {
		this.materialGroup = materialGroup;
	}
	public String getBaseUOM() {
		return baseUOM;
	}
	public void setBaseUOM(String baseUOM) {
		this.baseUOM = baseUOM;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getExMerchandiseCat() {
		return exMerchandiseCat;
	}
	public void setExMerchandiseCat(String exMerchandiseCat) {
		this.exMerchandiseCat = exMerchandiseCat;
	}
	public String getMaterialStatus() {
		return materialStatus;
	}
	public void setMaterialStatus(String materialStatus) {
		this.materialStatus = materialStatus;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getTempCondIndicator() {
		return tempCondIndicator;
	}
	public void setTempCondIndicator(String tempCondIndicator) {
		this.tempCondIndicator = tempCondIndicator;
	}
	public String getZZSZQTY() {
		return zzszqty;
	}
	public void setZZSZQTY(String zzszqty) {
		this.zzszqty = zzszqty;
	}
	public String getSizeUOM() {
		return sizeUOM;
	}
	public void setSizeUOM(String sizeUOM) {
		this.sizeUOM = sizeUOM;
	}
	public String getLOB() {
		return LOB;
	}
	public void setLOB(String lOB) {
		LOB = lOB;
	}
	public String getParticipant() {
		return participant;
	}
	public void setParticipant(String participant) {
		this.participant = participant;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemowner() {
		return itemowner;
	}
	public void setItemowner(String itemowner) {
		this.itemowner = itemowner;
	}
	public void setC(String c) {
		this.c=c;
	}
	public String getC() {
		
		return c;
	}
	public String getConsecutiveNumber() {
		return consecutiveNumber;
	}
	public void setConsecutiveNumber(String consecutiveNumber) {
		this.consecutiveNumber = consecutiveNumber;
	}
	public String getGTIN() {
		return gtin;
	}
	public void setGTIN(String gtin) {
		this.gtin = gtin;
	}
	public String getGTINCategory() {
		return gtinCategory;
	}
	public void setGTINCategory(String gtinCategory) {
		this.gtinCategory = gtinCategory;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getDimensionUnit() {
		return dimensionUnit;
	}
	public void setDimensionUnit(String dimensionUnit) {
		this.dimensionUnit = dimensionUnit;
	}
	public String getWeightUnit() {
		return weightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}
	public String getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(String grossWeight) {
		this.grossWeight = grossWeight;
	}
	public String getSalesOrg() {
		return salesOrg;
	}
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
	public String getDistributionChannel() {
		return distributionChannel;
	}
	public void setDistributionChannel(String distributionChannel) {
		this.distributionChannel = distributionChannel;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setValue(double d) {
		this.value=d;
		
	}
	public double getValue() {
		return value;
		
	}
	public String getProcessIndicator() {
		return processIndicator;
	}
	public void setProcessIndicator(String processIndicator) {
		this.processIndicator = processIndicator;
	}
	public String getArticleFileName() {
		return articleFileName;
	}
	public void setArticleFileName(String articleFileName) {
		this.articleFileName = articleFileName;
	}
	public void setArticleUpdated(String articleUpdated) {
		this.articleUpdated=articleUpdated;
		
	}
	public void setUpdatedTime(Date articleUpdatedTime) {
		this.articleUpdatedTime=articleUpdatedTime;
		
	}
	public void setArticleCreationDate(Date creationDate) {
		this.creationDate=creationDate;
		
	}
	
}
