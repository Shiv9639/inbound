package com.lcl.scs.dallas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lcl.scs.dallas.config.D30ConnectionObject;
import com.lcl.scs.dallas.config.D50ConnectionObject;
import com.lcl.scs.dallas.dao.DetailDao;
import com.lcl.scs.dallas.model.DallasDetail;
import com.lcl.scs.util.logging.LoggingUtilities;



public class DallasDetailJdbcDao{


	public void batchInsert(List<DallasDetail> t, int batch) throws Exception {

		Connection connection = D30ConnectionObject.getConnection();
		String sqlStatement = "insert into OUTBOUND_ORDER_DETAIL (OUTBOUND_ORDER, STORE, CARTON, DALLAS_PRODUCT, DALLAS_PROD_DESC, UPC, PROD_RETAIL_QTY, PROD_CUBE, PROD_WGT, SHIP_PACK_SIZE, CASE_UNIT, FILE_REFERENCE, ALLOCATED_QUANTITY) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int count  = 0;
		int[] result;
		PreparedStatement ps = null;
	
		try {
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sqlStatement);
			
			for(DallasDetail detail: t){
				ps.setString(1, detail.getOutbound_order());
				ps.setString(2, detail.getStore());
				ps.setString(3, detail.getCarton());
				ps.setString(4, detail.getDallas_product());
				ps.setString(5, detail.getDallas_prod_desc());
				ps.setString(6, detail.getUpc());
				if(detail.getProd_retail_qty().isEmpty()) {
					ps.setNull(7, Types.NUMERIC);
				}else {
					ps.setInt(7, Integer.parseInt(detail.getProd_retail_qty()));
				}
				if(detail.getProd_cube().isEmpty()) {
					ps.setNull(8, Types.NUMERIC);
				}else {
					ps.setDouble(8, Double.parseDouble(detail.getProd_cube()));
				}
				if(detail.getProd_wgt().isEmpty()) {
					ps.setNull(9, Types.NUMERIC);
				}else {
					ps.setDouble(9, Double.parseDouble(detail.getProd_wgt()));
				}
				if(detail.getShip_pack_size().isEmpty()) {
					ps.setNull(10, Types.NUMERIC);
				}else {
					ps.setInt(10, Integer.parseInt(detail.getShip_pack_size()));
				}
				if(detail.getCase_unit().isEmpty()) {
					ps.setNull(11, Types.NUMERIC);
				}else {
					ps.setInt(11, Integer.parseInt(detail.getCase_unit()));
				}
				ps.setString(12, detail.getFile_reference());
				if(detail.getAllocated_quantity().isEmpty()) {
					ps.setNull(13, Types.NUMERIC);
				}else {
					ps.setInt(13, Integer.parseInt(detail.getAllocated_quantity()));
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
	
	public void batchInsertD50(List<DallasDetail> t, int batch) throws Exception {

		
		Connection connection = D50ConnectionObject.getConnection();
		String sqlStatement = "insert into OUTBOUND_ORDER_DETAIL (OUTBOUND_ORDER, STORE, CARTON, DALLAS_PRODUCT, DALLAS_PROD_DESC, UPC, PROD_RETAIL_QTY, PROD_CUBE, PROD_WGT, SHIP_PACK_SIZE, CASE_UNIT, FILE_REFERENCE, ALLOCATED_QUANTITY) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int count  = 0;
		int[] result;
		PreparedStatement ps = null;
	
		try {
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(sqlStatement);
			
			for(DallasDetail detail: t){
				ps.setString(1, detail.getOutbound_order());
				ps.setString(2, detail.getStore());
				ps.setString(3, detail.getCarton());
				ps.setString(4, detail.getDallas_product());
				ps.setString(5, detail.getDallas_prod_desc());
				ps.setString(6, detail.getUpc());
				if(detail.getProd_retail_qty().isEmpty()) {
					ps.setNull(7, Types.NUMERIC);
				}else {
					ps.setInt(7, Integer.parseInt(detail.getProd_retail_qty()));
				}
				if(detail.getProd_cube().isEmpty()) {
					ps.setNull(8, Types.NUMERIC);
				}else {
					ps.setDouble(8, Double.parseDouble(detail.getProd_cube()));
				}
				if(detail.getProd_wgt().isEmpty()) {
					ps.setNull(9, Types.NUMERIC);
				}else {
					ps.setDouble(9, Double.parseDouble(detail.getProd_wgt()));
				}
				if(detail.getShip_pack_size().isEmpty()) {
					ps.setNull(10, Types.NUMERIC);
				}else {
					ps.setInt(10, Integer.parseInt(detail.getShip_pack_size()));
				}
				if(detail.getCase_unit().isEmpty()) {
					ps.setNull(11, Types.NUMERIC);
				}else {
					ps.setInt(11, Integer.parseInt(detail.getCase_unit()));
				}
				ps.setString(12, detail.getFile_reference());
				if(detail.getAllocated_quantity().isEmpty()) {
					ps.setNull(13, Types.NUMERIC);
				}else {
					ps.setInt(13, Integer.parseInt(detail.getAllocated_quantity()));
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
		
		
		
		//System.out.println("Inserted " + count +  " to the db");
	}
	
	
	

}
