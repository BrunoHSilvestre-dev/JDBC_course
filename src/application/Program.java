package application;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import db.DB;
import db.DBException;
import db.DBIntegrityException;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		SellerDao sellerDao = DaoFactory.createSellerDao();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("=== Test 01 - Seller - FindById ===");
		System.out.println(sellerDao.findById(3));
		System.out.println();
		
		System.out.println("=== Test 02 - Seller - FindByDepartment(2) ===");
		sellerDao.findByDepartment(departmentDao.findById(2)).forEach(System.out::println);
		System.out.println();
		
		System.out.println("=== Test 03 - Seller - FindAll ===");
		sellerDao.findAll().forEach(System.out::println);
		System.out.println();
		
		System.out.println("=== Test 04 - Seller - Insert ===");
//		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", LocalDate.now(), 4000.0, departmentDao.findById(2));
//		sellerDao.insert(newSeller);
//		System.out.println("Inserted with success. New Id: " + newSeller.getId());
		
		System.out.println("=== Test 05 - Seller - Update ===");
		
		Seller newSeller = sellerDao.findById(11);
		newSeller.setName("GregCris");
		newSeller.setEmail("greg@cris.com");
		newSeller.setBirthDate(LocalDate.now());
		newSeller.setBaseSalary(5000.0);
		newSeller.setDepartment(departmentDao.findById(3));
		
		sellerDao.update(newSeller);
		
		DB.closeConnection();
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
	
	private static void transactionExample() {
		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection();
			
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			int rows1 = st.executeUpdate("update seller set BaseSalary = 2090 where DepartmentId = 1");
			
//			if (true) {
//				throw new SQLException("Fake error");
//			}
			
			int rows2 = st.executeUpdate("update seller set BaseSalary = 3090 where DepartmentId = 2");
			
			conn.commit();
			
			System.out.println("Rows 1: " + rows1);
			System.out.println("Rows 2: " + rows2);
			
		} catch (SQLException e) {
			try {
				conn.rollback();
				throw new DBException("Transaction rolledback, reason: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DBException("Error trying to rollback, reason: " + e1.getMessage());
			}
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
