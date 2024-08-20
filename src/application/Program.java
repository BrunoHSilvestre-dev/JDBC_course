package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import db.DB;

public class Program {

	public static void main(String[] args) {
//		Connection conn = DB.getConnection();	
//		DB.closeConnection();
		
//		selectExample();
//		insertExample();
		updateExample();
	}

	private static void selectExample() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			conn = DB.getConnection();
			
			st = conn.createStatement();
			
			rs = st.executeQuery("select * from department");
			
			while (rs.next()) {
				System.out.println(rs.getInt("Id") + " " + rs.getString("Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
	
	private static void insertExample() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DB.getConnection();
			
			ps = conn.prepareStatement(
				"insert into seller " + 
				"(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
				"values " + 
				"(?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);
			
			ps.setString(1, "Carl Purple");
			ps.setString(2, "carl@gmail.com");
//			ps.setDate(3, new java.sql.Date(0));
//			ps.setObject(3, LocalDateTime.now());
			ps.setDate(3, Date.valueOf(LocalDate.now()));
			ps.setDouble(4, 3000.0);
			ps.setInt(5, 4);
			
			int rowsAffected = ps.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
			
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				
				while (rs.next()) {
					System.out.println("Key: " + rs.getInt(1));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeConnection();
		}
	}
	
	private static void updateExample() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DB.getConnection();
			
			ps = conn.prepareStatement(
				"update seller " + 
				"set BaseSalary = BaseSalary + ? " +
				"where DepartmentId = ? "
			);
			
			ps.setDouble(1, 100);
			ps.setInt(2, 3);
			
			int rowsAffected = ps.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(ps);
			DB.closeConnection();
		}
	}
}
