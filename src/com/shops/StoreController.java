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
	MongoDAO mongoDAO;
	ArrayList<Store> stores;
	ArrayList<Product> products;
	ArrayList<StoresProducts> storesProds;
	ArrayList<StoreHeadOffice> storeHOs;
	
	public StoreController() {
		super();
		
		try {
			this.dao = new DAO();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			this.mongoDAO = new MongoDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}//StoreController
	
	
	
	public ArrayList<Store> getStores() {
		return stores;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public ArrayList<StoresProducts> getStoresProds() {
		return storesProds;
	}

	public ArrayList<StoreHeadOffice> getStoreHOs() {
		return storeHOs;
	}



	public void loadStores() {
		System.out.println("in loadStores()");
		try {
			stores = this.dao.loadStores();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//loadStores
	
	public void loadProducts() {
		try {
			products = this.dao.loadProducts();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//loadProducts
	
	public void deleteProduct(Product p) {
		System.out.println(p.getProdName());
		
		try {
			dao.deleteProduct(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Innovation method    /////////////////////////////////////////////////////
	public String addProduct(Product p) throws Exception {
		// Get last store in Array list to get id 
		Product lastProduct = this.products.get(products.size()-1);
		int incPid = lastProduct.getPid() + 1;
		// Increment id and add it to new store object 
		p.setPid(incPid);
		
		dao.addProduct(p);
		return "list_products";
	
	}
	//////////////////////////////////////////////////////////////////////////////

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
					new FacesMessage("Error: Store "+s.getName()+" already exists");  // Constraint on Name of store and founded
					FacesContext.getCurrentInstance().addMessage(null, message);

		}
		catch(CommunicationsException e) {
			FacesMessage message = 
					new FacesMessage("Error: Cannot connect to MySQL Database");
					FacesContext.getCurrentInstance().addMessage(null, message);
		}
		catch(Exception e) {
			FacesMessage message = 
					new FacesMessage("Error: "+e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, message);
			
		}
		
		return null;
	}
	
	public String deleteStore(Store s) {
		System.out.println(s.getName());
		boolean delete = true;
		// check to see if store has products ; if sid == true , store still has products
		if(products == null) {
			try {
				products = this.dao.loadProducts();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(int i=0; i< products.size(); i++) {  										
			if(s.getId() == products.get(i).sid) {
				// product for store exists; therefore can't be deleted 
				delete = false;
				break;
			}
		}
		
		
		if(delete == true) {
			try {
				dao.deleteStore(s);
			} catch (Exception e) {
				FacesMessage message = 
						new FacesMessage("Error: "+e.getMessage());
						FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}else {
			FacesMessage message = 
					new FacesMessage("Error: Store "+s.getName()+" has not been deleted from MySQL DB, it contains products");
					FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return null;
	}

	public void storesProducts(Store s) {
		try {
			storesProds = dao.storesProducts(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void loadHeadOffices() {
		System.out.println("in controller in LoadHeadOffices");
		storeHOs = this.mongoDAO.loadHeadOffices();
	}
	
	public String addHeadOffice(StoreHeadOffice s) {
		boolean locationExists = false;
		boolean idExists = false;
		// get stores to check ids to see if Head office will be valid 
		try {
			stores = this.dao.loadStores();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<stores.size(); i++) {
			if(s.get_id() == stores.get(i).getId()) {
				// id exists , add office 
				idExists = true;
				locationExists = mongoDAO.addHeadOffice(s);
			}
			
		}//for
		
		if(locationExists == true) {
				FacesMessage message = 
						new FacesMessage("Error: Store ID: "+s.get_id()+" already has a location");
						FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		if(idExists == true && locationExists == false) {
			System.out.println("added head office");
			return "list_offices";
		}
		else if(idExists == false){
			FacesMessage message = 
					new FacesMessage("Error: Store ID: "+s.get_id()+" does not exist");
					FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return null;
	}
	
	
}
