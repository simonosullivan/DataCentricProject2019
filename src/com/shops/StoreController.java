package com.shops;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.CommunicationsException;



@ManagedBean
@SessionScoped
public class StoreController {

	DAO dao;
	ArrayList<Store> stores;
	ArrayList<Product> products;
	
	public StoreController() {
		super();
		
		try {
			this.dao = new DAO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//StoreController
	
	
	
	public ArrayList<Store> getStores() {
		return stores;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}
	

	public void loadStores() {
		System.out.println("in loadStores()");
		try {
			stores = this.dao.loadStores();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//loadStores
	
	public void loadProducts() {
		try {
			products = this.dao.loadProducts();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//loadProducts


	public String addStore(Store s) {
		System.out.println(s.getName()+" "+s.getFounded());
		
		// Get last store in Array list to get id 
		Store lastStore = this.stores.get(stores.size()-1);
		int incId = lastStore.getId() + 1;
		// Increment id and add it to new store object 
		s.setId(incId);
		
		try {
			dao.addStore(s);
			return "list_stores";
		} catch(SQLIntegrityConstraintViolationException e) {
			FacesMessage message = 
					new FacesMessage("Error: Id already exists");  // Constraint on Name of store and founded
					FacesContext.getCurrentInstance().addMessage(null, message);

		}
		catch(CommunicationsException e) {
			FacesMessage message = 
					new FacesMessage("Error: Can't communicate with DB");
					FacesContext.getCurrentInstance().addMessage(null, message);
		}
		catch(Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: "+e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, message);
			
		}
		
		return null;
	}

	
	
}
