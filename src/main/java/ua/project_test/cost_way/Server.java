package ua.project_test.cost_way;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Server implements Runnable {
	
	private Thread t;
	private float arCost[][] = 
			  {{0,1,0,0,0,0,0,0,0,0},
			  {1,0,4,0,2,0,3,10,9,0},
			  {0,4,0,5,0,0,0,0,0,0},
			  {0,0,5,0,0,0,0,0,0,0},
			  {0,2,0,0,0,6,0,0,0,0},
			  {0,0,0,0,6,0,0,0,0,7},
			  {0,3,0,0,0,0,0,0,0,0},
			  {0,10,0,0,0,0,0,0,9,0},
			  {0,0,0,0,0,0,0,9,0,8},
			  {0,0,0,0,0,7,0,0,8,0}};
	private Request r;
	private Sender sender;
	private int port;
	
	public Server(int port){
		
		this.port = port;
		t = new Thread(this, "Server");
		this.r = new Request();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("error");
			System.exit(-1);
		}
		//sender = new Sender("email", "password");  // connect to mail server
		t.start();
	}
	
	private void calculate(BasicDBObject b){
		
		float price = 0;
				
		if((boolean)b.get("is_out")){
			List<DBObject> list = r.find((Integer)b.get("us_id"));
			list.add(b);
			for(int i = 0; i < (list.size()-1); i++){
					price += arCost[(int)list.get(i).get("number_point")-1][(int)list.get(i+1).get("number_point")-1];
			}
			r.insertTravel(list);
			r.removeFromCurrent((Integer)b.get("us_id"));
			sendLetter(list, price);
		}else{
			r.insertcurrentTravel(b);
		}
	}
	
	private void sendLetter(List<DBObject> list, float p) {
		
		String s = ""; 
		for(DBObject b: list){
			s += "Checkpoint: " + b.get("number_point") + " Date: " +(String)b.get("date") + "\n";
		}
		DBObject user = r.findUser((Integer)list.get(0).get("us_id"));
		String text = "Good afternoon, Mr. "+ user.get("name") +"\nThank you for using our road :)\nYour route:\n" + s + "The cost of voyage: " + p + "$";
		System.out.print(text); 
		
		//sender.send("You journey", text, "email", (String)user.get("email")); //send mail
	}

	private BasicDBObject parseString(String s){	
		
		BasicDBObject b = BasicDBObject.parse(s);		
		return b;		
	}

	public void run(){
		
 	    BufferedReader in = null;
 	    PrintWriter out = null;

 	    ServerSocket servers = null;
 	    Socket fromclient = null;
 	    float g = 0;

 	    try {
 	    	servers = new ServerSocket(port);
 	    } catch (IOException e) {
 	    	e.printStackTrace();
 	    	System.out.println("Couldn't listen to port 4444");
 	    	System.exit(-1);
 	    }

 	    try {
 	    	System.out.print("Waiting for a client...");
 	    	fromclient= servers.accept();
 	      	System.out.println("Client connected");
 	    } catch (IOException e) {
 	    	System.out.println("Can't accept");
 	    	System.exit(-1);
 	    }

 	    try{
 	  	    in = new BufferedReader(new InputStreamReader(fromclient.getInputStream()));
 	  	    out = new PrintWriter(fromclient.getOutputStream(),true);
 	   	    String input,output;
 	   	    int i=0;
 	   	    System.out.println("Wait for messages");
 	   	    while(true){
 	   	    	input = in.readLine();
 	   	    	if(input.equalsIgnoreCase("exit")){
	   	    		break;
	   	    	}
 	   	    	calculate(parseString(input));
 	   	    }
 	   	    out.println("exit");
 	   	    out.close();
 	   	    in.close();
 	   	    fromclient.close();
 	   	    servers.close();
 	   	    
 	   } catch(IOException e){
 		   System.out.println("Can't accept");
 		   System.exit(-1);
	    }
	}

}
