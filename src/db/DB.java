package db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	public static Connection getConnection() {
		if (conn == null) {
			Properties props = loadProperties();
			
			String url = props.getProperty("dburl");
			
			try {
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
		
		return conn;
	}
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		Properties props = new Properties();
		
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			props.load(fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new DBException(e.getMessage());
		}
		
		return props;
	}
}
