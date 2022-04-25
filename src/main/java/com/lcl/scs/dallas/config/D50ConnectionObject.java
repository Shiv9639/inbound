package com.lcl.scs.dallas.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class D50ConnectionObject {
	private static Connection conn = null;
	
	public static Connection getConnection() {
		String url = System.getenv("ODS_DALLAS_URL");
		String username = System.getenv("ODS_D50_USERNAME");
		String pass = System.getenv("ODS_D50_PASSWORD");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, username, pass);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;		
	}
}
