package com.shops;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.google.gson.Gson;
//import com.mongodb.BasicDBObject;


public class MongoDAO {
	
	String mongoDB = "storeHeadOfficeDB";
	String mongoCollection = "storeHeadOffice";
	
	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> collection;
	
	ArrayList<StoreHeadOffice> cpHeadOffices = new ArrayList<>();
	
	public void setCpHeadOffices(ArrayList<StoreHeadOffice> cpHeadOffices) {
		this.cpHeadOffices = cpHeadOffices;
	}


	public ArrayList<StoreHeadOffice> getCpHeadOffices() {
		return cpHeadOffices;
	}


	public MongoDAO() throws Exception {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("storeHeadOfficeDB");
		collection = database.getCollection("storeHO");
	}


	public ArrayList<StoreHeadOffice> loadHeadOffices() {
		System.out.println("in mongoDAO in loadHeadOffices");
		cpHeadOffices.clear();
		
		Gson gson = new Gson();
		FindIterable<Document> headOffice = collection.find();
		ArrayList<StoreHeadOffice> list = new ArrayList<>();
		
		for (Document d : headOffice) {
			   StoreHeadOffice sho = gson.fromJson(d.toJson(), StoreHeadOffice.class);
			   list.add(sho);
			   cpHeadOffices.add(sho);
		}
		
		return list;
	}


	public boolean addHeadOffice(StoreHeadOffice s) {
		boolean exists = false;
		
		
		
		 // check if _id is being used already 
		for(int i=0; i<cpHeadOffices.size(); i++) {
			 if(s.get_id() == cpHeadOffices.get(i).get_id()) { 
				 exists = true; 
			 }
		}
		 
		
		
		
		if(exists == false) {
			Document myDoc = new Document();
			myDoc.append("_id", s.get_id())
		     .append("location", s.getLocation());
			collection.insertOne(myDoc);
			
			return false;
		
		}
		else { 
			System.out.println("_id already exists "); 
			return true;
		}
		 

	}

	
	
}
