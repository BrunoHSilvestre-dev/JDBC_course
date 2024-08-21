package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	@Override
	public List<Seller> findAll(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT seller.*,department.Name as DepName " + 
				"FROM seller INNER JOIN department " + 
				"ON seller.DepartmentId = department.Id " + 
				"WHERE seller.DepartmentId = ?"	
			);
			
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<Integer, Department>();	
			
			while (rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep != null) {
					dep = instanciateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				list.add(instanciateSeller(rs, dep));
			}
			
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Seller instanciateSeller(ResultSet rs) throws SQLException {
		return instanciateSeller(rs, instanciateDepartment(rs));
	}
	
	private Seller instanciateSeller(ResultSet rs, Department dep) throws SQLException {
		return new Seller(
			rs.getInt("Id"),
			rs.getString("Name"),
			rs.getString("Email"),
			LocalDateTime.parse(rs.getString("BirthDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate(),
			rs.getDouble("BaseSalary"),
			dep
		);
	}
	
	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		return new Department(
			rs.getInt("DepartmentId"), 
			rs.getString("DepName")
		);
	}
}
