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
	
	
	/* ======================================================================================================
	 * Constructor
	 * ====================================================================================================== */
	public MongoDAO() throws Exception {
		mongoClient = new MongoClient();
		database = mongoClient.getDatabase("storeHeadOfficeDB");
		collection = database.getCollection("storeHO");
	}


	public ArrayList<StoreHeadOffice> loadHeadOffices() {
		System.out.println("in mongoDAO in loadHeadOffices");
		//BasicDBObject query = new BasicDBObject();
		
		ArrayList<StoreHeadOffice> list = new ArrayList<>();
		Gson gson = new Gson();
		FindIterable<Document> headOffice = collection.find();
		
		for (Document d : headOffice) {
			   StoreHeadOffice sho = gson.fromJson(d.toJson(), StoreHeadOffice.class);
			   list.add(sho);
		}
		System.out.println("List : "+list);
		return list;
	}
	
	
}
