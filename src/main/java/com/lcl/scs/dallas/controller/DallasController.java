package com.lcl.scs.dallas.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.lcl.scs.dallas.dao.DallasDetailJdbcDao;
import com.lcl.scs.dallas.dao.DallasHeaderJdbcDao;
import com.lcl.scs.dallas.dao.DetailDao;
import com.lcl.scs.dallas.dao.HeaderDao;
import com.lcl.scs.dallas.model.*;

public class DallasController {
	private DallasDetailJdbcDao detailDao = new DallasDetailJdbcDao();
	
	private DallasHeaderJdbcDao headerDao = new DallasHeaderJdbcDao();
	
//	public DallasController(HeaderDao headerDao, DetailDao detailDao) {
//		this.headerDao = headerDao;
//		this.detailDao = detailDao;
//	}
//	public DallasController() {
//		super();
//	}
	
	
	public void insertHeaderToODS(List<DallasHeader> headers, int batchNumber) throws Exception {
		if(headers == null || headers.isEmpty()) {
			//System.out.println("headers is empty");
		}else{
			headerDao.batchInsert(headers, batchNumber);
		}
	}
	
	public void insertDetailToODS(List<DallasDetail> details, int batchNumber) throws Exception {
		if(details == null || details.isEmpty()) {
			//System.out.println("details is empty");
		}else {
			detailDao.batchInsert(details, batchNumber);
		}
		
	}
	
	public void insertD50HeaderToODS(List<DallasHeader> headers, int batchNumber) throws Exception {
		if(headers == null || headers.isEmpty()) {
			//System.out.println("headers is empty");
		}else{
			headerDao.batchInsertD50(headers, batchNumber);
		}
	}
	
	public void insertD50DetailToODS(List<DallasDetail> details, int batchNumber) throws Exception {
		if(details == null || details.isEmpty()) {
			//System.out.println("details is empty");
		}else {
			detailDao.batchInsertD50(details, batchNumber);
		}
		
	}
	
	public List<DallasHeader> readHeaderCSV(String file, String fileName) throws IOException, ParseException{
		BufferedReader br = new BufferedReader(new StringReader(file));
		String line = "";
		List<DallasHeader> header = new ArrayList<>();
		
		while((line = br.readLine()) != null) {
			String[] attributes = line.split(",");
			
			DallasHeader dallasHdr = createHeader(attributes, fileName);
			
			header.add(dallasHdr);
		}

		return header;
	
	
	}
	public List<DallasDetail> readDetailCSV(String file, String fileName) throws IOException{
		BufferedReader br = new BufferedReader(new StringReader(file));
		String line = "";
		
		List<DallasDetail> detail = new ArrayList<>();
		
		while((line = br.readLine()) != null) {
			String[] attributes = line.split(",");
			
			DallasDetail dallasdt = createDetail(attributes, fileName);
			
			detail.add(dallasdt);
		}
		
		return detail;
	}

	private DallasDetail createDetail(String[] attributes, String fileName) {
		
		String col1 = attributes[0];
		String col2 = attributes[1];
		String col3 = attributes[2];
		String col4 = attributes[3];
		String col5 = attributes[4];
		String col6 = attributes[5];
		String alloc_qty = (attributes[6]);
		String col7 = (attributes[7]);
		String col8 = (attributes[8]);
		String col9 = (attributes[9]);
		String col10 = (attributes[10]);
		String col11 = (attributes[11]);
		String fileRef = fileName.substring(fileName.length()-20, fileName.length()-4);
		
		
		return new DallasDetail(col1, col2, col3, col4, col5, col6, col7, col8, col9, col10, col11, fileRef, alloc_qty);
	}
	private DallasHeader createHeader(String[] attributes, String fileName) throws ParseException {
		String store = attributes[0];
		String MMS_PO = attributes[1];
		String outbound_order = attributes[2];
		
		Date delivery_date;
		try {
			delivery_date = new java.sql.Date(new SimpleDateFormat("MM/dd/yyyy").parse(attributes[3]).getTime());
		} catch (ParseException e) {
			delivery_date = null;
		}
		String delivery_time = attributes[4];
		String DC = attributes[5];
		String LW = attributes[6];
		String missing_col = attributes[7];
		String order_status = attributes[8];
		Date order_routed_date_time;
		try {
			order_routed_date_time = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attributes[9]).getTime());
		} catch (ParseException e) {
			order_routed_date_time = null;
		}
		String route = attributes[10];
		String stop = attributes[11];
		Date order_create_date_time;
		try {
			order_create_date_time = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attributes[12]).getTime());
		} catch (ParseException e) {
			order_create_date_time = null;
		}
		Date order_change_date_time;
		try {
			order_change_date_time = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attributes[13]).getTime());
		} catch (ParseException e) {
			order_change_date_time = null;
		}
		String carton = attributes[14];
		String carton_status = attributes[15];
		String ship_method_type = attributes[16];
		String ship_method_desc = attributes[17];
		String carton_cube = attributes[18];
		String carton_weight = attributes[19];
		String alloc_qty = attributes[20];
		String carton_units = attributes[21];
		String carton_lines = attributes[22];
		Date carton_create_date_time;
		try {
			if(attributes[23].isEmpty()) {
				carton_create_date_time = null;
			}else {
				carton_create_date_time = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attributes[23]).getTime());
			}
		} catch (ParseException e) {
			carton_create_date_time = null;
		}
//		Date carton_create_date_time = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attributes[23]).getTime());
		String fileRef = fileName.substring(fileName.length()-20, fileName.length()-4);
		String BOL = attributes[24];
		String fedex_tracking_number = attributes[25];
		String carrier = attributes[26];
		String outbound_order_quantity = attributes[27];
		
		return new DallasHeader(store, MMS_PO, outbound_order, delivery_date, delivery_time, DC, LW, missing_col, order_status, order_routed_date_time, route, stop, 
				order_create_date_time, order_change_date_time, carton, carton_status, ship_method_type, ship_method_desc, carton_cube, carton_weight, carton_units, carton_lines, carton_create_date_time, fileRef,
				BOL, fedex_tracking_number, carrier, alloc_qty, outbound_order_quantity);
		}
	}
