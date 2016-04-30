package org.three60.createdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DBConnection {
	
	Connection con = null;
	Statement stmt = null;
	
	public DBConnection(){
		 try {
			 String password = "root";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ssr", "root", password);
			stmt = con.createStatement();
		 } catch (Exception e) {
			 JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			 e.printStackTrace();
		 }
	}
	
	public ResultSet select(String query){
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			stmt.execute(query);
			rs = stmt.getResultSet(); 
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public int insert(String query) throws SQLException{
		int status = 0;
		try {
			status = stmt.executeUpdate(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return status;
	}

	public int createTable(String query) throws SQLException{
		int status = 0;
		try {
			status = stmt.executeUpdate(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return status;
	}
	
	public int delete(String query){
		int status = 0;
		try {
			status = stmt.executeUpdate(query);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
	
	public void closeConnection(){
		try {
			stmt.close();
			con.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "DB : "+e.getMessage());
			e.printStackTrace();
		}
	}
}
