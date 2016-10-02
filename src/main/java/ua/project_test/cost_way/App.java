package ua.project_test.cost_way;

import java.util.ArrayList;
import java.util.List;

import ua.project_test.cost_way.Client;
import ua.project_test.cost_way.Server;


public class App 
{
    public static void main( String[] args ) 
    {    	
    	Server s = new Server(4444);
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("error");
		}
    	List<Integer> i = new ArrayList<Integer>();
    	if(args.length > 0){             
    		for(String e : args)
    		i.add(Integer.parseInt(e));
    	}else{
    		i.add(1);
    		i.add(2);
    		i.add(5);
    		i.add(6);
    		i.add(10);
    	}
    	
    	Client c = new Client("localhost",4444,i);
    }
    
    public void exitProgram()
    {
        System.exit(0);
    }
}
