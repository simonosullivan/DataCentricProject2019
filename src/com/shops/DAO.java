package com.shops;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class DAO {

	private DataSource mysqlDS;

	
	/* ======================================================================================================
	 * Constructor
	 * ====================================================================================================== */
	public DAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/shops";
		mysqlDS = (DataSource) context.lookup(jndiName);
	}


	public ArrayList<Store> loadStores() throws Exception {

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select * from store";

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);
		
		ArrayList<Store> stores = new ArrayList<>();
		
		// process result set
		while (myRs.next()) {
			Store s = new Store();
			s.setId(myRs.getInt("id"));
			s.setName(myRs.getString("name"));
			s.setFounded(myRs.getString("founded"));
			stores.add(s);
		}
		
		return stores;
	}


	public ArrayList<Product> loadProducts() throws Exception{

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();

		String sql = "select * from product";

		myStmt = myConn.createStatement();

		myRs = myStmt.executeQuery(sql);
		
		ArrayList<Product> products = new ArrayList<>();
		
		// process result set
		while (myRs.next()) {
			Product p = new Product();
			p.setPid(myRs.getInt("pid"));
			p.setSid(myRs.getInt("sid"));
			p.setProdName(myRs.getString("prodName"));
			p.setPrice(myRs.getDouble("price"));
			products.add(p);
		}
		
		return products;
	}
	
	public void addStore(Store store) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "insert into store values (?, ?, ?)";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, store.getId()); 
		myStmt.setString(2, store.getName());
		myStmt.setString(3, store.getFounded());
		myStmt.execute();
	}


	public void deleteStore(Store store) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "delete from store where id=?;";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, store.getId());
		myStmt.execute();
	}


	public void deleteProduct(Product p) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "delete from product where pid=?;";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, p.getPid());
		myStmt.execute();
	}


	public ArrayList<StoresProducts> storesProducts(Store s) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "select s.id, s.name, s.founded, p.pid, p.prodName, p.price from store s join product p on s.id = p.sid where s.id=?;";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, s.getId());
		myRs = myStmt.executeQuery();
		
		ArrayList<StoresProducts> storesProds = new ArrayList<>();
		
		// process result set
		while (myRs.next()) {
			StoresProducts sp = new StoresProducts();
			sp.setId(myRs.getInt("id"));
			sp.setName(myRs.getString("name"));
			sp.setFounded(myRs.getString("founded"));
			sp.setPid(myRs.getInt("pid"));
			sp.setProdName(myRs.getString("prodName"));
			sp.setPrice(myRs.getDouble("price"));
			
			storesProds.add(sp);
		}
		return storesProds;
		
	}

	
	// Innovation method     ///////////////////////////////////
	public void addProduct(Product p) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		myConn = mysqlDS.getConnection();
		String sql = "insert into product values (?, ?, ?, ?)";
		myStmt = myConn.prepareStatement(sql);
		myStmt.setInt(1, p.getPid()); 
		myStmt.setInt(2, p.getSid());
		myStmt.setString(3, p.getProdName());
		myStmt.setDouble(4, p.getPrice());
		myStmt.execute();
	}
	////////////////////////////////////////////////////////////
	

}
