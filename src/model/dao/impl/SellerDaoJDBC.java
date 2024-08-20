package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller department) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName " + 
				"FROM seller INNER JOIN department " + 
				"ON seller.DepartmentId = department.Id " + 
				"WHERE seller.Id = ?"	
			);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				return instanciateSeller(rs);
						
			}
			return null;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Seller instanciateSeller(ResultSet rs) throws SQLException {
		return new Seller(
			rs.getInt("Id"),
			rs.getString("Name"),
			rs.getString("Email"),
			LocalDateTime.parse(rs.getString("BirthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate(),
			rs.getDouble("BaseSalary"),
			new Department(rs.getInt("DepartmentId"), rs.getString("DepName"))
		);
	}

	@Override
	public List<Seller> findAll(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
