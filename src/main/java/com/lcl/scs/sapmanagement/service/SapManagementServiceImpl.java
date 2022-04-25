package com.lcl.scs.sapmanagement.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.lcl.scs.exception.BadRequestException;
import com.lcl.scs.sapmanagement.constants.SapManagementConstants;
import com.lcl.scs.sapmanagement.model.ArticleMaster;
import com.lcl.scs.sapmanagement.model.FlatData;
import com.lcl.scs.sapmanagement.model.FlatMasterData;
import com.lcl.scs.sapmanagement.model.Site;
import com.lcl.scs.sapmanagement.model.StoreMaster;
import com.lcl.scs.sapmanagement.model.StoreMasterId;
import com.lcl.scs.sapmanagement.model.VendorMaster;
import com.lcl.scs.sapmanagement.repository.ArticleMasterRepository;
import com.lcl.scs.sapmanagement.repository.FlatDataRepository;
import com.lcl.scs.sapmanagement.repository.SiteRepository;
import com.lcl.scs.sapmanagement.repository.StoreMasterRepository;
import com.lcl.scs.sapmanagement.repository.VendorMasterRepository;
import com.lcl.scs.util.logging.LoggingUtilities;

@Service
public class SapManagementServiceImpl implements SapManagementService {

	private static final SimpleDateFormat DATETIMEFORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");
	@Autowired
	private FlatDataRepository flatDataRepo;
	boolean articleFilterFlag = false;
	@Autowired
	private StoreMasterRepository storeMasterRepo;
	@Autowired
	private ArticleMasterRepository articleMasterRepo;
	@Autowired
	private VendorMasterRepository vendorMasterRepository;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	private ArticleMaster articleMaster;
	private VendorMaster vendorMaster;

	HashMap<String, String> westStates = new HashMap<String, String>();
	HashMap<String, String> atlanticStates = new HashMap<String, String>();
	HashMap<String, String> countries = new HashMap<String, String>();

	@Override
	public String processMessage(String interfaceId, String fileName, String xmlMessage, String contentType) {
		String result = storeFlatMessage(interfaceId, fileName, xmlMessage, contentType);
		if (result.equals(SapManagementConstants.PAYLOAD_SUCCESS_MSG)) {
			return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
		}
		return result;
	}

	public String processXMLMessage(String interfaceId, String fileName, String xmlMessage, String contentType) {
		/*
		 * log.info(
		 * "Entering in SapManagementService---->processSapMessage method to process the data"
		 * );
		 */

		String result = storeFlatMessage(interfaceId, fileName, xmlMessage, contentType);
		if (result.equals(SapManagementConstants.PAYLOAD_SUCCESS_MSG)) {
			return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
		}
		return result;

	}

	public String processFlatMessage(String interfaceId, String fileName, String payload, String contentType) {
		LoggingUtilities.generateInfoLog("Entering in SapManagementService---->processFlatMessage method");
		String result = storeFlatMessage(interfaceId, fileName, payload, contentType);
		if (result.equals(SapManagementConstants.PAYLOAD_SUCCESS_MSG)) {
			return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
		}
		return result;
	}

	private String storeFlatMessage(String interfaceId, String fileName, String payload, String contentType) {

		// CompositeKey compositeKey = new CompositeKey(interfaceId,
		// fileName.split("\\.")[0]);

		try {
			// FlatData flatData = new FlatData(compositeKey, fileName.split("\\.")[0]);

			String fName = fileName.split("\\.")[0];
			String contType = "application/xml".equalsIgnoreCase(contentType) ? "xml" : "text";
			String interfaceDesc = "";
			String destColl = "";

			switch (interfaceId) {
			case SapManagementConstants.SITEXREF_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.SITEXREF_INTERFACE_DESC;
				destColl = SapManagementConstants.SITEXREF_DEST_COLL;
				saveFileContent(interfaceId, payload, fName, contType, interfaceDesc, destColl);
				LoggingUtilities.generateInfoLog("Process content:" + SapManagementConstants.SITEXREF_INTERFACE_ID);

				processSiteXRefData(payload, contType);
				break;
			case SapManagementConstants.SITE_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.SITE_INTERFACE_DESC;
				destColl = SapManagementConstants.SITE_DEST_COLL;
				saveFileContent(interfaceId, payload, fName, contType, interfaceDesc, destColl);
				LoggingUtilities.generateInfoLog("Process content:" + SapManagementConstants.SITE_INTERFACE_ID);
				processSiteData(payload, contentType);
				break;

			case SapManagementConstants.ARTICLE_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.ARTICLE_INTERFACE_DESC;
				destColl = SapManagementConstants.ARTICLE_DEST_COLL;
				saveFileContentArticle(interfaceId, payload, fName, contType, interfaceDesc, destColl);
				// LoggingUtilities.generateInfoLog("Process content:" +
				// SapManagementConstants.ARTICLE_INTERFACE_ID);
				break;
			case SapManagementConstants.CUSTOMER_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.CUSTOMER_INTERFACE_DESC;
				destColl = SapManagementConstants.CUSTOMER_DEST_COLL;
				saveFileContent(interfaceId, payload, fName, contType, interfaceDesc, destColl);
				// LoggingUtilities.generateInfoLog("Process content:" +
				// SapManagementConstants.ARTICLE_INTERFACE_ID);
				break;
			case SapManagementConstants.CUSTOMER_DELTA_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.CUSTOMER_DELTA_INTERFACE_DESC;
				destColl = SapManagementConstants.CUSTOMER_DELTA_INTERFACE_COLL;
				saveFileContent(interfaceId, payload, fName, contType, interfaceDesc, destColl);
				break;
			case SapManagementConstants.VENDOR_INTERFACE_ID:
				interfaceDesc = SapManagementConstants.VENDOR_INTERFACE_DESC;
				destColl = SapManagementConstants.VENDOR_DEST_COLL;
				saveVendorFileContent(interfaceId, payload, fName, contType, interfaceDesc, destColl);
//				processVendorMasterData(payload, contType);
				// LoggingUtilities.generateInfoLog("Process content:" +
				// SapManagementConstants.ARTICLE_INTERFACE_ID);
				break;
		

			default:
				break;
			}

		} catch (Exception e) {
			LoggingUtilities.generateErrorLog("Exception while processing flat message : {}" + e.getMessage());
			throw new BadRequestException("Failed while saving:" + "interfaceId:" + interfaceId + "::filename:"
					+ fileName + "::message:" + e.getMessage());
			// return "Failed while saving";
		}

		return SapManagementConstants.PAYLOAD_SUCCESS_MSG;
	}

	private void saveFileContentArticle(String interfaceId, String payload, String fName, String contType,
			String interfaceDesc, String destColl) {
		FlatMasterData flatData = new FlatMasterData();
		flatData.setFileName(fName);
		// flatData.setInterfaceId(interfaceId);
		flatData.setInterfaceDesc(interfaceDesc);
		flatData.setDestCollection(destColl);
		flatData.setContent(payload);
		flatData.setContentType(contType);
		// flatDataRepo.save(flatData);
		mongoTemplate.save(flatData, interfaceId);
		LoggingUtilities.generateInfoLog(
				"File:" + fName + "::interfaceId:" + interfaceId + "::contentType:" + contType + " is saved");
		LoggingUtilities.generateInfoLog("Start copying payload to ArticleMaster");
		processArticleMasterData(payload, contType);
	}

	private void saveFileContent(String interfaceId, String payload, String fName, String contType,
			String interfaceDesc, String destColl) {
		FlatData flatData = new FlatData();
		flatData.setFileName(fName);
		// flatData.setInterfaceId(interfaceId);
		flatData.setInterfaceDesc(interfaceDesc);
		flatData.setDestCollection(destColl);
		flatData.setContent(payload);
		flatData.setContentType(contType);
		// flatDataRepo.save(flatData);
		mongoTemplate.save(flatData, interfaceId);
		LoggingUtilities.generateInfoLog(
				"File:" + fName + "::interfaceId:" + interfaceId + "::contentType:" + contType + " is saved");
	}
	private void saveVendorFileContent(String interfaceId, String payload, String fName, String contType,
			String interfaceDesc, String destColl) {
		FlatMasterData flatVendorData = new FlatMasterData();
		flatVendorData.setFileName(fName);
		// flatData.setInterfaceId(interfaceId);
		flatVendorData.setInterfaceDesc(interfaceDesc);
		flatVendorData.setDestCollection(destColl);
		flatVendorData.setContent(payload);
		flatVendorData.setContentType(contType);
		// flatDataRepo.save(flatData);
		mongoTemplate.save(flatVendorData, interfaceId);
		LoggingUtilities.generateInfoLog(
				"File:" + fName + "::interfaceId:" + interfaceId + "::contentType:" + contType + " is saved");
		LoggingUtilities.generateInfoLog("Start sending payload to Vendor Master");
		processVendorMasterData(payload, contType);
	}
	private String processSiteData(String payload, String contentType) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			Site site = new Site();
			db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(new StringReader(payload)));
			doc.getDocumentElement().normalize();

			NodeList wERKSList = doc.getElementsByTagName("WERKS");
			for (int x = 0; x < wERKSList.getLength(); x++) {
				site.setLocation(wERKSList.item(x).getTextContent());
			}

			NodeList regionList = doc.getElementsByTagName("REGION");
			for (int x = 0; x < regionList.getLength(); x++) {
				site.setRegion(regionList.item(x).getTextContent());
			}

			site.setDateUpdated(new Date());
			siteRepository.save(site);

		} catch (ParserConfigurationException e) {
			LoggingUtilities.generateErrorLog("ERROR_PARSING_XML:" + e.getMessage());
			throw new BadRequestException(e.getMessage());
		} catch (SAXException e) {
			LoggingUtilities.generateErrorLog("ERROR_PARSING_XML:" + e.getMessage());
			throw new BadRequestException(e.getMessage());
		} catch (IOException e) {
			LoggingUtilities.generateErrorLog("ERROR_PARSING_XML:" + e.getMessage());
			throw new BadRequestException(e.getMessage());
		}

		return "success";

	}

	private String processSiteXRefData(String payload, String contentType) {

		try {
			if ("text".equalsIgnoreCase(contentType)) {
				List<String> rowsList = new ArrayList<>();
				StringTokenizer rows = new StringTokenizer(payload);
				while (rows.hasMoreTokens()) {
					rowsList.add(rows.nextToken());
				}
				List<StoreMaster> storeMasterList = new ArrayList<StoreMaster>();

				rowsList.forEach(row -> {
					StoreMaster storeMaster = new StoreMaster();
					Iterable<String> splitResult = Splitter.on("|").split(row);
					List<String> result = Lists.newArrayList(splitResult);
					storeMaster.setExtractDateTime(result.get(0));
					storeMaster.setMappingType(result.get(1));
					StoreMasterId id = new StoreMasterId();
					id.setInternalLocationNumber(result.get(2));
					id.setExternalLocationNumber(result.get(4));
					storeMaster.setId(id);
					// storeMaster.setInternalLocationNumber(result.get(2));
					storeMaster.setInternalLocationType(result.get(3));
					// storeMaster.setExternalLocationNumber(result.get(4));
					storeMaster.setExternalLocationType(result.get(5));
					storeMaster.setExternalGroup(result.get(6));
					storeMaster.setActiveDate(result.get(7));
					storeMaster.setOpenDate(result.get(8));
					storeMaster.setCloseDate(result.get(9));
					storeMaster.setDeactiveDate(result.get(10));
					storeMaster.setOperationDateTime(result.get(11));
					storeMaster.setActiveIndicator(result.get(12));
					storeMaster.setOpenIndicator(result.get(13));
					if (result.size() >= 23) {
						storeMaster.setSoleProprietor(result.get(22));

					}
					// storeMasterList.add(storeMaster);
					storeMasterRepo.save(storeMaster);

				});
				/*
				 * BulkOperations bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED,
				 * "StoreMaster"); storeMasterList.stream().filter(entry -> entry !=
				 * null).forEach(entry -> { DBObject dbDoc = new BasicDBObject();
				 * mongoTemplate.getConverter().write(entry, (Bson) dbDoc); Update update = new
				 * Update(); update.fromDocument(new Docu., exclude) //
				 * dbDoc.removeField("_id"); Query q = new
				 * Query(Criteria.where("_id").is(dbDoc.get("_id"))); bulkOps.upsert(q, update);
				 * 
				 * BulkWriteRequestBuilder bulkWriteRequestBuilder =
				 * bulkOps.find(Criteria.apply(dbDoc).getCriteriaObject());
				 * BulkUpdateRequestBuilder upsertReq = bulkWriteRequestBuilder.upsert();
				 * 
				 * 
				 * });
				 * 
				 * BulkWriteResult result = bulkOps.execute();
				 */

				// final BulkWriteOptions bulkOps =
				// mongoTemplate.getCollection.("StoreMaster").;
				/*
				 * if(storeMasterList !=null && !storeMasterList.isEmpty()){
				 * mongoTemplate.insert(storeMasterList,"StoreMaster" );
				 * LoggingUtilities.generateInfoLog("Saved xml into StoreMaster");; }
				 */
			}

			return "Success";

		} catch (Exception e) {
			LoggingUtilities.generateErrorLog(e.getMessage());
			throw new BadRequestException("Failed while saving");
		}

	}

	private void processArticleMasterData(String payload, String contentType) {
        
		try {
			LoggingUtilities.generateInfoLog("check if the content type is text");
			if ("text".equalsIgnoreCase(contentType)) {
				List<String> rowsList = new ArrayList<>();
				StringTokenizer rows = new StringTokenizer(payload,System.lineSeparator());
				while (rows.hasMoreTokens()) {
					rowsList.add(rows.nextToken());
				}
     			//LoggingUtilities.generateInfoLog(payload);
				LoggingUtilities.generateInfoLog("Size"+rowsList.size());
				rowsList.forEach(row -> {
					//LoggingUtilities.generateInfoLog("processing articles");
					//LoggingUtilities.generateInfoLog("row"+row);
					Iterable<String> splitResult = Splitter.on("|").split(row);
					List<String> result = Lists.newArrayList(splitResult);

					if ("MARA".equalsIgnoreCase(result.get(0))) {
						ArticleMaster existingArticle = articleMasterRepo.findByarticleNumber("" + result.get(1));

						// check if the article already exists
						if (existingArticle != null) {
							articleMaster = existingArticle;
							articleFilterFlag = true;
						} else {
							articleFilterFlag = false;
							articleMaster = new ArticleMaster();
							articleMaster.setArticleNumber(result.get(1));
						}

						articleMaster.setMaterialType(result.get(2));
						articleMaster.setMaterialGroup(result.get(4));
						articleMaster.setBaseUOM(result.get(6));
						articleMaster.setC(result.get(7));
						articleMaster.setBrandId(result.get(8));
						articleMaster.setExMerchandiseCat(result.get(10));
						articleMaster.setMaterialStatus(result.get(11));
						articleMaster.setValidFrom(result.get(12));
						articleMaster.setCreatedOn(result.get(13));
						articleMaster.setTempCondIndicator(result.get(15));
						articleMaster.setZZSZQTY(result.get(18));
						articleMaster.setSizeUOM(result.get(19));
					}
					if ("MAKT".equalsIgnoreCase(result.get(0)) && "E".equalsIgnoreCase(result.get(2))) {

						articleMaster.setArticleDescription(result.get(3));

					}
					if ("MEAN".equalsIgnoreCase(result.get(0)) && articleMaster.getC().equalsIgnoreCase(result.get(2))) {

						articleMaster.setConsecutiveNumber(result.get(3));
						articleMaster.setGTIN(result.get(4));
						articleMaster.setGTINCategory(result.get(5));
						articleMaster.setIndicator(result.get(6));

					}
					if ("MARM".equalsIgnoreCase(result.get(0))
							&& articleMaster.getC().equalsIgnoreCase(result.get(2))) {

						articleMaster.setValue(1 / Double.parseDouble(result.get(3)));
						articleMaster.setLength(result.get(4));
						articleMaster.setWidth(result.get(5));
						articleMaster.setHeight(result.get(6));
						articleMaster.setDimensionUnit(result.get(7));
						articleMaster.setWeightUnit(result.get(8));
						articleMaster.setGrossWeight(result.get(9));

					}
					if ("MVKE".equalsIgnoreCase(result.get(0))) {

						articleMaster.setSalesOrg(result.get(2));
						articleMaster.setDistributionChannel(result.get(3));

					}
					if ("MAW1".equalsIgnoreCase(result.get(0))) {

						articleMaster.setCountry(result.get(3));

					}
					if ("MAMT".equalsIgnoreCase(result.get(0)) && "F".equalsIgnoreCase(result.get(2))) {
						
						try {
							// Check if the article is present and processed then update and send to LCT
							if (articleFilterFlag) {
								articleMaster.setArticleUpdated("Y");
								articleMaster.setUpdatedTime(new Date());
							}
							else
							{
								articleMaster.setArticleCreationDate(new Date());
							}
						} catch (Exception ex) {

							LoggingUtilities.generateErrorLog(ex.getMessage());

						}
						articleMasterRepo.save(articleMaster);
						// articleMasterList.add(articleMaster);
						LoggingUtilities.generateInfoLog(articleMaster.getArticleNumber()+" Article Payload added successfully");
					}

				});

			}
			else {
				LoggingUtilities.generateInfoLog("Article Payload type is not text");
			}

			

		} catch (Exception e) {
			LoggingUtilities.generateErrorLog(e.getMessage());
			throw new BadRequestException("Failed while saving");
		}

	}

	private String processVendorMasterData(String payload, String contType) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory doc = DocumentBuilderFactory.newInstance();
		DocumentBuilder ven;

		try {
			// canadian states
			westStates.put("1", "BC");
			westStates.put("2", "AB");
			westStates.put("4", "MB");
			westStates.put("5", "SK");
			atlanticStates.put("6", "NB");
			atlanticStates.put("7", "NS");
			atlanticStates.put("8", "NL");
			atlanticStates.put("9", "PEI");
			// countries
			countries.put("1", "Canada");
			countries.put("2", "USA");
			countries.put("3", "United States of America");
			countries.put("4", "Mexico");
			boolean vendorFilterFlag = false;

			ven = doc.newDocumentBuilder();

			Document doc1 = ven.parse(new InputSource(new StringReader(payload)));
			doc1.getDocumentElement().normalize();

			NodeList lifnr = doc1.getElementsByTagName("LIFNR");
			NodeList numvendors = doc1.getElementsByTagName("TOTAL1");
			NodeList name = doc1.getElementsByTagName("NAME1");
			NodeList city = doc1.getElementsByTagName("ORT01");
			// tag empty to be populated once SAP sends feed
			NodeList addresses = doc1.getElementsByTagName("STRAS");
			NodeList country = doc1.getElementsByTagName("LAND1");
			NodeList regions = doc1.getElementsByTagName("REGIO");
			NodeList codes = doc1.getElementsByTagName("PSTLZ");
			NodeList accounts = doc1.getElementsByTagName("KTOKK");

			int e = 0;
			String vendors = numvendors.item(0).getTextContent();
			LoggingUtilities.generateInfoLog(" Number of Vendors: " + Integer.parseInt(vendors));

			for (int x = 0; x < Integer.parseInt(vendors); x++) {				
				VendorMaster vendorFromDB = vendorMasterRepository.findBySite(lifnr.item(e).getTextContent());
//				checking if vendor already exists
				if (vendorFromDB != null) {
					vendorMaster = vendorFromDB;
					vendorFilterFlag = true;
				} else {
				vendorFilterFlag = false;
				vendorMaster = new VendorMaster();
				}
				vendorMaster.setSite(lifnr.item(e).getTextContent());
				vendorMaster.setParticipant(vendorMaster.getSite());
				vendorMaster.setSiteOwnerName(vendorMaster.getSite());
//				LoggingUtilities.generateInfoLog(" vendor id is " + vendorMaster.getSite());
				e=e+4;
				vendorMaster.setSiteName(name.item(x).getTextContent());
				vendorMaster.setAddressDescriptor(vendorMaster.getSiteName());
				vendorMaster.setAddressLine1(vendorMaster.getSiteName());

				vendorMaster.setSiteType("Factory");
				vendorMaster.setSiteOwnerType("Tenant");
				vendorMaster.setLocationType("Plant");
			
				vendorMaster.setCity(((city.item(x).getTextContent())));
				vendorMaster.setRegionHrCity(((city.item(x).getTextContent())));

				if (((country.item(x).getTextContent())).equalsIgnoreCase("CA")) {
					vendorMaster.setCountry("Canada");
					vendorMaster.setRegionHrCountry("Canada");
				} else {
					vendorMaster.setCountry(((country.item(x).getTextContent())));
					vendorMaster.setRegionHrCountry(((country.item(x).getTextContent())));
				}

				vendorMaster.setState(((regions.item(x).getTextContent())));
				vendorMaster.setRegionHrState(((regions.item(x).getTextContent())));

				if (westStates.containsValue(vendorMaster.getState())) {
					vendorMaster.setRegion("West");
				} else if (atlanticStates.containsValue(vendorMaster.getState())) {
					vendorMaster.setRegion("Atlantic");
				} else if (vendorMaster.getState().equalsIgnoreCase("ON")) {
					vendorMaster.setRegion("Ontario");
				} else if (vendorMaster.getState().equalsIgnoreCase("QC")) {
					vendorMaster.setRegion("Quebec");
				} else if (countries.containsValue(vendorMaster.getCountry())) {
					vendorMaster.setRegion("North America");
				} else {
					vendorMaster.setRegion("Overseas");
				}

				vendorMaster.setLatitude("");
				vendorMaster.setLongitude("");

				vendorMaster.setZipCode((codes.item(x).getTextContent()));

				if (countries.containsValue(vendorMaster.getCountry())) {
					vendorMaster.setContinent("North America");
				} else
					vendorMaster.setContinent("Overseas");

				vendorMaster.setPiComputeRequired("No");
				vendorMaster.setEarlyDelToleranceDays("0.041666667");
				vendorMaster.setLateDelToleranceDays("0.041666667");
				vendorMaster.setDwellTime("1");

				
				vendorMaster.setAccountGroup(accounts.item(x).getTextContent());
				
				if(vendorFilterFlag==true) {
					vendorMaster.setVendorUpdatedIndicator("Y");
					vendorMaster.setVendorUpdatedTime(new Date());
					vendorMaster.setStatus("Active");
				}else {
				vendorMaster.setVendorUpdatedIndicator("N");
				vendorMaster.setStatus("On Hold");
				vendorMaster.setVendorCreatedDate(new Date());
				}

				vendorMasterRepository.save(vendorMaster);
			}
			LoggingUtilities.generateInfoLog("Saved vendors");
		} catch (Exception e) {
			LoggingUtilities.generateErrorLog("Exception while processing vendor master message : {}" + e.getMessage());
		}
		return "Success";
	}

}

