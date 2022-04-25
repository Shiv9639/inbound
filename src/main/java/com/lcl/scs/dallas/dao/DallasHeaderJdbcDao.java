package com.lcl.scs.dallas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lcl.scs.dallas.config.D30ConnectionObject;
import com.lcl.scs.dallas.config.D50ConnectionObject;
import com.lcl.scs.dallas.model.DallasHeader;
import com.lcl.scs.util.logging.LoggingUtilities;


public class DallasHeaderJdbcDao{
	

	public void batchInsert(List<DallasHeader> t, int batch) throws Exception {

		
		Connection connection = D30ConnectionObject.getConnection();
		
		String sqlStatement = "insert into OUTBOUND_ORDER_HEADER (STORE, MMS_PO, OUTBOUND_ORDER, DELIVERY_DATE, DELIVERY_TIME, DC, LW, MMS_VENDOR_CODE, ORDER_STATUS, ORDER_STATUS_CHANGE_DATE_TIME, "
				+ "ROUTE, STOP, ORDER_CREATE_DATE_TIME, ORDER_CHANGE_DATE_TIME, CARTON, CARTON_STATUS, SHIP_METHOD_TYPE, SHIP_METHOD_DESC, CARTON_CUBE, CARTON_WEIGHT, CARTON_UNITS, CARTON_LINES, CARTON_CREATE_DATE_TIME, FILE_REFERENCE"
				+ ", BOL, FEDEX_TRACKING_NUMBER, CARRIER, ALLOCATED_QUANTITY, OUTBOUND_ORDER_QUANTITY)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int count  = 0;
		PreparedStatement ps = null;
		int[] result;
		
		
		try {
			connection.setAutoCommit(false);
			
			ps = connection.prepareStatement(sqlStatement);
			
			for(DallasHeader header : t) {
				ps.setString(1, header.getStore());
				ps.setString(2, header.getMMS_PO());
				ps.setString(3, header.getOutbound_order());
				if(header.getDelivery_date() == null) {
					ps.setNull(4, Types.DATE);
				}else{
					ps.setDate(4, header.getDelivery_date());
				}
				ps.setString(5, header.getDelivery_time());
				ps.setString(6, header.getDC());
				ps.setString(7, header.getLW());
				ps.setString(8, header.getMms_vendor_code());
				ps.setString(9, header.getOrder_status());
				if(header.getOrder_routed_date_time() == null) {
					ps.setNull(10, Types.DATE);
				}else {
					ps.setDate(10, header.getOrder_routed_date_time());
				}
				
				if(header.getRoute().isEmpty()) {
					ps.setNull(11, Types.NUMERIC);
				}else {
					ps.setInt(11, Integer.parseInt(header.getRoute()));
				}
				if(header.getStop().isEmpty()) {
					ps.setNull(12, Types.NUMERIC);
				}else {
					ps.setInt(12, Integer.parseInt(header.getStop()));
				}
				if(header.getOrder_create_date_time() == null) {
					ps.setNull(13, Types.DATE);
				}else {
					ps.setDate(13, header.getOrder_create_date_time());
				}
				if(header.getOrder_change_date_time() == null) {
					ps.setNull(14, Types.DATE);
				}else {
					ps.setDate(14, header.getOrder_change_date_time());
				}
				
				ps.setString(15, header.getCarton());
				ps.setString(16, header.getCarton_status());
				ps.setString(17, header.getShip_method_type());
				ps.setString(18, header.getShip_method_desc());
				if(header.getCarton_cube().isEmpty()) {
					ps.setNull(19, Types.NUMERIC);
				}else {
					ps.setDouble(19, Double.parseDouble(header.getCarton_cube()));
				}
				if(header.getCarton_weight().isEmpty()) {
					ps.setNull(20, Types.NUMERIC);
				}else {
					ps.setDouble(20, Double.parseDouble(header.getCarton_weight()));
				}
				if(header.getCarton_units().isEmpty()) {
					ps.setNull(21, Types.NUMERIC);
				}else {
					ps.setInt(21, Integer.parseInt(header.getCarton_units()));
				}
				if(header.getCarton_lines().isEmpty()) {
					ps.setNull(22, Types.NUMERIC);
				}else {
					ps.setInt(22, Integer.parseInt(header.getCarton_lines()));
				}
				if(header.getCarton_create_date_time() == null) {
					ps.setNull(23, Types.DATE);
				}else {
					ps.setDate(23, header.getCarton_create_date_time());
				}
				ps.setString(24, header.getFile_reference());
				ps.setString(25, header.getBOL());
				ps.setString(26, header.getFedex_tracking_number());
				ps.setString(27, header.getCarrier());
				if(header.getAllocated_quatity().isEmpty()) {
					ps.setNull(28, Types.NUMERIC);
				}else {
					ps.setInt(28, Integer.parseInt(header.getAllocated_quatity()));
				}
				if(header.getOutbound_order_quantity().isEmpty()) {
					ps.setNull(29, Types.NUMERIC);
				}else {
					ps.setInt(29, Integer.parseInt(header.getOutbound_order_quantity()));
				}
				ps.addBatch();
				count++;
				
				if(count % batch == 0) {
					result = ps.executeBatch();
					connection.commit();
				}
				
			}
			
			ps.executeBatch();
		} catch (Exception e) {
			LoggingUtilities.generateErrorLog("Failed to insert to the SQL tables" +  e.getMessage());
			e.printStackTrace();
			throw(e);
		}finally {
			if(ps != null)
				ps.close();
			if(connection != null)
				connection.close();
		}
		
		
		
	}
	
public void batchInsertD50(List<DallasHeader> t, int batch) throws Exception {

		
		Connection connection = D50ConnectionObject.getConnection();
		
		String sqlStatement = "insert into OUTBOUND_ORDER_HEADER (STORE, MMS_PO, OUTBOUND_ORDER, DELIVERY_DATE, DELIVERY_TIME, DC, LW, MMS_VENDOR_CODE, ORDER_STATUS, ORDER_STATUS_CHANGE_DATE_TIME, "
				+ "ROUTE, STOP, ORDER_CREATE_DATE_TIME, ORDER_CHANGE_DATE_TIME, CARTON, CARTON_STATUS, SHIP_METHOD_TYPE, SHIP_METHOD_DESC, CARTON_CUBE, CARTON_WEIGHT, CARTON_UNITS, CARTON_LINES, CARTON_CREATE_DATE_TIME, FILE_REFERENCE"
				+ ", BOL, FEDEX_TRACKING_NUMBER, CARRIER, ALLOCATED_QUANTITY, OUTBOUND_ORDER_QUANTITY) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int count  = 0;
		PreparedStatement ps = null;
		try {
			int[] result;
			
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sqlStatement);
			
			for(DallasHeader header : t) {
				ps.setString(1, header.getStore());
				ps.setString(2, header.getMMS_PO());
				ps.setString(3, header.getOutbound_order());
				if(header.getDelivery_date() == null) {
					ps.setNull(4, Types.DATE);
				}else{
					ps.setDate(4, header.getDelivery_date());
				}
				ps.setString(5, header.getDelivery_time());
				ps.setString(6, header.getDC());
				ps.setString(7, header.getLW());
				ps.setString(8, header.getMms_vendor_code());
				ps.setString(9, header.getOrder_status());
				if(header.getOrder_routed_date_time() == null) {
					ps.setNull(10, Types.DATE);
				}else {
					ps.setDate(10, header.getOrder_routed_date_time());
				}
				
				if(header.getRoute().isEmpty()) {
					ps.setNull(11, Types.NUMERIC);
				}else {
					ps.setInt(11, Integer.parseInt(header.getRoute()));
				}
				if(header.getStop().isEmpty()) {
					ps.setNull(12, Types.NUMERIC);
				}else {
					ps.setInt(12, Integer.parseInt(header.getStop()));
				}
				if(header.getOrder_create_date_time() == null) {
					ps.setNull(13, Types.DATE);
				}else {
					ps.setDate(13, header.getOrder_create_date_time());
				}
				if(header.getOrder_change_date_time() == null) {
					ps.setNull(14, Types.DATE);
				}else {
					ps.setDate(14, header.getOrder_change_date_time());
				}
				
				ps.setString(15, header.getCarton());
				ps.setString(16, header.getCarton_status());
				ps.setString(17, header.getShip_method_type());
				ps.setString(18, header.getShip_method_desc());
				if(header.getCarton_cube().isEmpty()) {
					ps.setNull(19, Types.NUMERIC);
				}else {
					ps.setDouble(19, Double.parseDouble(header.getCarton_cube()));
				}
				if(header.getCarton_weight().isEmpty()) {
					ps.setNull(20, Types.NUMERIC);
				}else {
					ps.setDouble(20, Double.parseDouble(header.getCarton_weight()));
				}
				if(header.getCarton_units().isEmpty()) {
					ps.setNull(21, Types.NUMERIC);
				}else {
					ps.setInt(21, Integer.parseInt(header.getCarton_units()));
				}
				if(header.getCarton_lines().isEmpty()) {
					ps.setNull(22, Types.NUMERIC);
				}else {
					ps.setInt(22, Integer.parseInt(header.getCarton_lines()));
				}
				if(header.getCarton_create_date_time() == null) {
					ps.setNull(23, Types.DATE);
				}else {
					ps.setDate(23, header.getCarton_create_date_time());
				}
				ps.setString(24, header.getFile_reference());
				ps.setString(25, header.getBOL());
				ps.setString(26, header.getFedex_tracking_number());
				ps.setString(27, header.getCarrier());
				if(header.getAllocated_quatity().isEmpty()) {
					ps.setNull(28, Types.NUMERIC);
				}else {
					ps.setInt(28, Integer.parseInt(header.getAllocated_quatity()));
				}
				if(header.getOutbound_order_quantity().isEmpty()) {
					ps.setNull(29, Types.NUMERIC);
				}else {
					ps.setInt(29, Integer.parseInt(header.getOutbound_order_quantity()));
				}
				
				ps.addBatch();
				count++;
				
				if(count % batch == 0) {
					result = ps.executeBatch();
					connection.commit();
				}
				
			}
		} catch (Exception e) {
			LoggingUtilities.generateErrorLog("Failed to insert to the SQL tables" +  e.getMessage());
			e.printStackTrace();
			throw(e);
		}finally {
			ps.executeBatch();
			
			if(ps != null)
				ps.close();
			if(connection != null)
				connection.close();
		}
		
		
		
	}

}
