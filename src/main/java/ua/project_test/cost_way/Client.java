package ua.project_test.cost_way;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

public class Client implements Runnable {
	
	private String adr;
	private Thread t;
	private List<Integer> list;
	private int port;
	
	public Client(String s, int port, List<Integer> list){
		this.port = port;
		this.adr = s;
		this.list = list;
		t = new Thread(this, "Client");
		t.start();
	}
	
	private String jsonRequest(int i){
		JSONObject resultJson = new JSONObject();
		boolean flag = false;
		if(i == list.size()-1){
			flag = true;
		}
		resultJson.put("us_id",new Integer(12345678));
		resultJson.put("is_out",new Boolean(flag)); //indicating car in/out 
		resultJson.put("number_point",list.get(i));
		resultJson.put("date", new Date().toString());
		return resultJson.toString();
	}
	
	public void run() {
	    	    
	    System.out.println("Connecting to... " + adr);

	    String fuser,fserver;
	    try(Socket fromserver = new Socket(adr, 4444)){
	    	BufferedReader in  = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
    		PrintWriter out = new PrintWriter(fromserver.getOutputStream(),true);
    		for(int i = 0; i < list.size(); i++){
    			fuser = jsonRequest(i);
	     		out.println(fuser);
	    	}
    		out.print("exit");
    		out.close();
    		in.close();
	    
	    }catch(IOException e){
	    	System.out.println("Couldn't connect to server");
 	    	System.exit(-1);
	    }
	    
	}

}
