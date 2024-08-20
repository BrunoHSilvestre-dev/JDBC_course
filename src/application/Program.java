package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import db.DB;
import db.DBIntegrityException;

public class Program {

	public static void main(String[] args) {
//		Connection conn = DB.getConnection();	
//		DB.closeConnection();
		
//		selectExample();
//		insertExample();
//		updateExample();
		deleteExample();
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
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();
			
			st = conn.prepareStatement(
				"insert into seller " + 
				"(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
				"values " + 
				"(?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);
			
			st.setString(1, "Carl Purple");
			st.setString(2, "carl@gmail.com");
//			st.setDate(3, new java.sql.Date(0));
//			st.setObject(3, LocalDateTime.now());
			st.setDate(3, Date.valueOf(LocalDate.now()));
			st.setDouble(4, 3000.0);
			st.setInt(5, 4);
			
			int rowsAffected = st.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				while (rs.next()) {
					System.out.println("Key: " + rs.getInt(1));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
	
	private static void updateExample() {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();
			
			st = conn.prepareStatement(
				"update seller " + 
				"set BaseSalary = BaseSalary + ? " +
				"where DepartmentId = ? "
			);
			
			st.setDouble(1, 100);
			st.setInt(2, 3);
			
			int rowsAffected = st.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
	
	private static void deleteExample() {
		Connection conn = null;
		PreparedStatement st = null;
		try {
			conn = DB.getConnection();
			
			st = conn.prepareStatement(
				"delete from department " + 
				"where Id = ? "
			);
			
			st.setInt(1, 2);
			
			int rowsAffected = st.executeUpdate();
			
			System.out.println("Rows affected: " + rowsAffected);
			
		} catch (SQLException e) {
			throw new DBIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
