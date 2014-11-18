package algorithm.ant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import algorithm.honeybee.HoneyBeeAlgorithm;
import algorithm.honeybee.HoneyBeeConstants;


public class AntAlgorithm {
	
	/*
	 * Pheromone table
	 * TODO: This is a temporary structure which would later be stored in database.
	 */
	private HashMap<Integer, Double> pheromoneTable = new HashMap<Integer, Double>();
	private double cpu;
	private double hd;
	private double ram;
	private int request;
	private static AntAlgorithm antAlgorithm;
	private HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationAverageResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	
	private AntAlgorithm() {
		int i;
		// Inserting into pheromone table
		for(i=1;i<=AntConstants.getInstance().getNoOfLocations();i++) {
			pheromoneTable.put(i, AntConstants.getInstance().getPheromone());
		}
	}
	
	
	public static synchronized AntAlgorithm getInstance() {
		if(antAlgorithm == null) {
			antAlgorithm = new AntAlgorithm();
		}
		return antAlgorithm;
	}
	
	
	public double getCpu() {
		return cpu;
	}


	public void setCpu(double cpu) {
		this.cpu = cpu;
	}


	public double getHd() {
		return hd;
	}


	public void setHd(double hd) {
		this.hd = hd;
	}


	public double getRam() {
		return ram;
	}


	public void setRam(double ram) {
		this.ram = ram;
	}


	public int getRequest() {
		return request;
	}


	public void setRequest(int request) {
		this.request = request;
	}


	public void processTimeLogForRequest(int request,int location,int requestType) throws Exception{
		String requestTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
		HashMap<Integer,List> reqTypeTimeStampHM=new HashMap<Integer,List>();
		List locationAndRequestTimeStampAL=new ArrayList();
		locationAndRequestTimeStampAL.add(0,location); //location
		locationAndRequestTimeStampAL.add(1,requestTimeStamp); //request timestamp
		locationAndRequestTimeStampAL.add(2,null); //response timestamp - default to null
		locationAndRequestTimeStampAL.add(3,null); //time difference in seconds - default to  null
		
		reqTypeTimeStampHM.put(requestType, locationAndRequestTimeStampAL); //requesttype[key]-location n time [values-List]
		
		reqResTimeLogTable.put(request,reqTypeTimeStampHM);
		System.out.println("reqResTimeLogTable in REQUEST: "+reqResTimeLogTable);
	}
	
	public void processTimeLogForResponse(int request, String responseTimeStamp, int requestType)throws Exception{
		System.out.println("reqResTimeLogTable in response: "+reqResTimeLogTable);
		reqResTimeLogTable.get(request).get(requestType).set(2, responseTimeStamp);
		System.out.println("reqResTimeLogTable after reset in response: "+reqResTimeLogTable);
	}
	
	public void calculateResponseTime(int request, int location,int requestType)throws Exception{
		try{
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS"); 
			Date d1 = format.parse(reqResTimeLogTable.get(request).get(requestType).get(1).toString());
		    Date d2 = format.parse(reqResTimeLogTable.get(request).get(requestType).get(2).toString());
		    long diffInMilliSeconds = d2.getTime() - d1.getTime();
		    double diffSeconds = diffInMilliSeconds / 1000.0;
		    System.out.println("Time in seconds: " + diffSeconds + " seconds.");  
		    reqResTimeLogTable.get(request).get(requestType).set(3, diffSeconds);
		    System.out.println("reqResTimeLogTable after update:::::::::: "+reqResTimeLogTable);
		    
		    System.out.println("locationResponseTimeLogTable before update: "+locationResponseTimeLogTable);
		    
//		    		Double newAvgResponseTime=(Double.parseDouble(locationResponseTimeLogTable.get(location).get(requestType).get(0).toString())+diffSeconds)/(Integer.parseInt(locationResponseTimeLogTable.get(location).get(requestType).get(1).toString())+1);
		    		
//		    		System.out.println("$$$$$$$$$$$$$  "+locationResponseTimeLogTable.get(location).get(requestType));
//				    locationResponseTimeLogTable.get(location).get(requestType).set(0, newAvgResponseTime);
//				    locationResponseTimeLogTable.get(location).get(requestType).set(1, Integer.parseInt(locationResponseTimeLogTable.get(location).get(requestType).get(1).toString())+1);
		    		locationResponseTimeLogTable.get(location).get(requestType).add(diffSeconds);
		    		List tempCountList=new ArrayList<>();
		    		tempCountList=locationResponseTimeLogTable.get(location).get(requestType);
		    		double timeCalc=0;
		    		for(int avgCalcCount=0;avgCalcCount<tempCountList.size();avgCalcCount++){
		    			timeCalc+=Double.parseDouble(tempCountList.get(avgCalcCount).toString());
		    		}
		    		double avgTime=timeCalc/tempCountList.size();
		    		locationAverageResponseTimeLogTable.get(location).get(requestType).set(0, avgTime);
		    		System.out.println("Average Response Time For "+requestType+" at location "+location+" is "+locationAverageResponseTimeLogTable.get(location).get(requestType).get(0));
		    		System.out.println("Number of request at location "+location+" for request type "+requestType+" : "+tempCountList.size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void printPheromoneTable() {
		int i;
		// Printing pheromone table
		for(i=1;i<=AntConstants.getInstance().getNoOfLocations();i++) {
			System.out.println("Location:" + i + " " + pheromoneTable.get(i));
		}
	}
	
	
	public int getLocationWithHighestPheromoneCount() {
		Iterator<Entry<Integer, Double>> it = pheromoneTable.entrySet().iterator();
		double maxValue = 0, minValue = AntConstants.getInstance().getPheromone();
		int location = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
			if(pair.getValue() > maxValue) {
				maxValue = pair.getValue();
				location = pair.getKey(); 
			}
			if(pair.getValue() < minValue) {
				minValue = pair.getValue();
			}
		}
		
		// If all locations have the same value. Then randomly select one location.
		if(maxValue == minValue) {
			Random random = new Random();
			location = random.nextInt(pheromoneTable.size()) + 1;
		}
		
		return location;
	}
	
	
	public void increasePheromoneCountOfLocation(int location) {
		
		double p = (pheromoneTable.get(location) + AntConstants.getInstance().getDeltaPheromone()) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		pheromoneTable.put(location, p);
	}
	
	
	public void decreasePheromoneCountOfLocation(int location) {
		
		double p = pheromoneTable.get(location) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		pheromoneTable.put(location, p);
	}
	
	
	/**
	 * The is the actual logic / algorithm.
	 * @return
	 */
	
	public int antBasedControl() {
		
		/*
		 * Algorithm steps:-
		 * 1. The centralized server will look at the pheromone table and check if there is any preference.
		 * 2. For the first time all will have the same probability. So server will send the request to any of the location.
		 * 3. The location which will send its response first after completing its job, 
		 *    will update the routing table with more preference and is the shortest path.
		 * 4. We will need to store the status of the request served as more the server request served has high pheromone as per the algorithm.
		 * 5. At the start server will send requests to all locations. The location having less response time will become suitable for 
		 * 	  next request.
		 */
		
		
		// Fetch the location with highest pheromone count.
		// This would be our candidate for processing request.
		int location = getLocationWithHighestPheromoneCount();
		System.out.println("Location having highest pheromone count: " + location);
		
		
		// Check whether there are sufficient amount of resource available on that location.
		if(!(AntConstants.getInstance().isCPUAvailable(location, cpu) && AntConstants.getInstance().isHDAvailable(location, hd) && AntConstants.getInstance().isRAMAvailable(location, ram))) {
			// If not decrease the pheromone count of that location and start the search again.
			decreasePheromoneCountOfLocation(location);
			location = antBasedControl();
		}
		
		
		if(locationResponseTimeLogTable.isEmpty()){
			System.out.println("locationResponseTimeLogTable is empty..");
			//put 0 in locationResponseTimeLogTable
//			ArrayList lst=new ArrayList<>();
			for(int respTempVar=1;respTempVar<=AntConstants.getInstance().getNoOfLocations();respTempVar++){
			HashMap<Integer,List> hmap=new HashMap<Integer,List>();
			for(int tempVar=0;tempVar<5;tempVar++){
//				if(tempVar<2){
				ArrayList lst=new ArrayList<>();
				/*lst.add(0);
				lst.add(0);*/
//				}
				hmap.put(tempVar, lst);
			}
			
//			for(int respTempVar=1;respTempVar<=HoneyBeeConstants.getInstance().getNoOfLocations();respTempVar++){
				locationResponseTimeLogTable.put(respTempVar, hmap);
			}
			
			//
			if(locationAverageResponseTimeLogTable.isEmpty()){
				System.out.println("locationAverageResponseTimeLogTable is empty..");
				//put 0 in locationAverageResponseTimeLogTable
				for(int respAvgTempVar=1;respAvgTempVar<=AntConstants.getInstance().getNoOfLocations();respAvgTempVar++){
				HashMap<Integer,List> avghmap=new HashMap<Integer,List>();
				for(int tempVar=0;tempVar<5;tempVar++){
//					if(tempVar<2){
					ArrayList avglst=new ArrayList<>();
					avglst.add(0);
					/*lst.add(0);
					lst.add(0);*/
//					}
					avghmap.put(tempVar, avglst);
				}
				
//				for(int respTempVar=1;respTempVar<=HoneyBeeConstants.getInstance().getNoOfLocations();respTempVar++){
				locationAverageResponseTimeLogTable.put(respAvgTempVar, avghmap);
				}
			}
			//
			
		}
		
		
		// TODO: Check how this algorithm works. If not able to distribute uniformly, uncomment below code.
		decreasePheromoneCountOfLocation(location);
		
		return location;
	}
} 
