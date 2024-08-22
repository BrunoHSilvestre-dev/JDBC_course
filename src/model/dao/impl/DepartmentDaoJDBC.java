package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"insert into department " + 
				"(Name)" +
				"values " + 
				"(?)",
				Statement.RETURN_GENERATED_KEYS
			);
			
			st.setString(1, department.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if (rs.next()) 
					department.setId(rs.getInt(1));
				
				DB.closeResultSet(rs);
			}
			else {
				throw new DBException("Unexpected Error! No rows affected!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"update department " + 
				"set Name = ? " +
				"where Id = ?"
			);
			
			st.setString(1, department.getName());
			
			st.setInt(2, department.getId());
			
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from department where Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT department.* " +
				"FROM department " +
				"WHERE department.Id = ?"	
			);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if (rs.next()) {
				return instanciateDepartment(rs);
			}
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}

	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		return new Department(
			rs.getInt("Id"),
			rs.getString("Name")
		);
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null; 
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM department"
			);
						
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<Department>();
			
			while (rs.next()) {
				list.add(instanciateDepartment(rs));
			}
			
			return list;
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
}
