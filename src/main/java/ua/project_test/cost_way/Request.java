package ua.project_test.cost_way;


import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Request {
	
	private DBCollection clientCollection;
	private DBCollection currentTravelsCollection;
	private DBCollection travelsCollection;
	
	public Request(){
		
		try{
			initDB();
		}catch(Exception e){
			System.out.println("bad");
		}
	}
	
	private void initDB()throws UnknownHostException{
		
		@SuppressWarnings("deprecation")
		Mongo mongo = new Mongo("127.0.0.1", 27017);
        @SuppressWarnings("deprecation")
		DB db = mongo.getDB("clientDB");
        BasicDBObject document = new BasicDBObject();
        clientCollection = db.getCollection("clients");
        if (clientCollection == null) {
        	clientCollection = db.createCollection("clients", null);
        	
        	document.put("_id", new Integer(12345678));
            document.put("name", "Bob");
            document.put("last_name", "J");
            document.put("email", "em@foo.ua");
           
        	clientCollection.insert(document);
        	
        }
        currentTravelsCollection = db.getCollection("currentTravels");
        if (currentTravelsCollection == null) {
        	currentTravelsCollection = db.createCollection("currentTravels", null);
        }
        
        travelsCollection = db.getCollection("travels");
        if (travelsCollection == null) {
        	travelsCollection = db.createCollection("travels", null);
        }
        
	}
	
	public List<DBObject> find(int id){
		
		DBObject b = new BasicDBObject();
		b.put("us_id", id);
		List<DBObject> list = new ArrayList<DBObject>();
		DBCursor cursor = currentTravelsCollection.find(b);	
		list = cursor.toArray();
			
		return list;
	}
	
	public DBObject findUser(int id){
		
		DBObject b = new BasicDBObject();
		b.put("_id", id);
	
		List<DBObject> list = new ArrayList<DBObject>();
		DBCursor cursor = clientCollection.find(b);
		
		list = cursor.toArray();
		return list.get(0);
	}
	
	public void insertTravel(BasicDBObject document){
		travelsCollection.insert(document);
	}
	
	public void insertTravel(List<DBObject> document){
		travelsCollection.insert(document);
	}
	
	public void insertcurrentTravel(BasicDBObject document){
		currentTravelsCollection.insert(document);
	}
	public void removeFromCurrent(int id){
		DBObject b = new BasicDBObject();
		b.put("us_id", id);
		currentTravelsCollection.remove(b);
	}
	
}

