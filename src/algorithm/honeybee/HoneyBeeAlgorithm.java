package algorithm.honeybee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;

public class HoneyBeeAlgorithm {

	
	/*
	 * Fitness table
	 * TODO: This is a temporary structure which would later be stored in database.
	 */
//	private HashMap<Integer, Double> fitnessTable = new HashMap<Integer, Double>();
	@SuppressWarnings("rawtypes")
	private HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
	@SuppressWarnings("rawtypes")
	public HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationAverageResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	
	private int request=0;
	private double cpu;
	private double hd;
	private double ram;
	private int requestType;
	private int tempCount=1;
	private boolean flag=false;
	
	private static HoneyBeeAlgorithm honeybeeAlgorithm;
	
	private HoneyBeeAlgorithm() {
		int i;
		// Inserting into fitness table
		for(i=1;i<=HoneyBeeConstants.getInstance().getNoOfLocations();i++) {
//			fitnessTable.put(i, HoneyBeeConstants.getInstance().getFitness());
		}
	}
	
	public static synchronized HoneyBeeAlgorithm getInstance() {
		if(honeybeeAlgorithm == null) {
			honeybeeAlgorithm = new HoneyBeeAlgorithm();
		}
		return honeybeeAlgorithm;
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
	
	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	
	
	public int getRequest() {
		return request;
	}

	public void setRequest(int request) {
		this.request = request;
	}
	
	
	public int processHoneyBeeAlgorithm(double cpu, double storage, double ram, double time,int requestType){

		HoneyBeeAlgorithm.getInstance().setCpu(cpu);
		HoneyBeeAlgorithm.getInstance().setHd(storage);
		HoneyBeeAlgorithm.getInstance().setRam(ram);
		HoneyBeeAlgorithm.getInstance().setRequestType(requestType);
		
		//aa.printPheromoneTable();
		
		System.out.println("After request");	
		//insufficientResourceLocation will be zero or have no value right now
		ArrayList<Integer> insufficientResourceLocation=new ArrayList<Integer>();
		int location = HoneyBeeAlgorithm.getInstance().honeybeeBasedControl(insufficientResourceLocation);
		
		//reduce the allocated resource
		HoneyBeeConstants.getInstance().increaseLocationDetails(location, HoneyBeeAlgorithm.getInstance().getCpu(), HoneyBeeAlgorithm.getInstance().getHd(), HoneyBeeAlgorithm.getInstance().getRam());
		System.out.println("Location having highest Fitness: " + location);
		
		
		System.out.println("-------------------------------------------------------");
		System.out.println("Selected Location details: "+location);
		System.out.println("Location CPU: "+HoneyBeeConstants.getInstance().getLocationCPU()+" occupied out of "+HoneyBeeConstants.getInstance().getLocationMaxCPU());
		System.out.println("Location HD: "+HoneyBeeConstants.getInstance().getLocationHD()+" occupied out of "+HoneyBeeConstants.getInstance().getLocationMaxHD());
		System.out.println("Location RAM: "+HoneyBeeConstants.getInstance().getLocationRAM()+" occupied out of "+HoneyBeeConstants.getInstance().getLocationMaxRAM());
		
		System.out.println("-------------------------------------------------------");
//		hbAlgorithm.printFitnessTable();
	
		return location;
	}

	/*public void printFitnessTable() {
		int i;
		// Printing fitness table
		for(i=1;i<=HoneyBeeConstants.getInstance().getNoOfLocations();i++) {
			System.out.println(fitnessTable.get(i));
		}
	}*/
	
	
	public int getLocationWithHighestFitnessValue(ArrayList<Integer> insufficientResourceLocation) {
		// find the least avg time for this request type and pick that location id from locationResponseTimeLogTable
		//iterate over outermost hashmap, get the avgtime value of particular request type from the inner map n arraylist
		
		int temp=0,location=1; //returning first since default value is 1 
	    double tempMinAvgVal=0;
	    HashMap<Integer, List> tempHM=new HashMap<Integer, List>();
		
		System.out.println("$$$$$$$$$$$insufficientResourceLocation: "+insufficientResourceLocation);
		
		if(locationResponseTimeLogTable.isEmpty()){
			System.out.println("locationResponseTimeLogTable is empty..");
			if(!(HoneyBeeConstants.getInstance().isCPUAvailable(location, HoneyBeeAlgorithm.getInstance().getCpu()) && HoneyBeeConstants.getInstance().isHDAvailable(location, HoneyBeeAlgorithm.getInstance().getHd()) && HoneyBeeConstants.getInstance().isRAMAvailable(location, HoneyBeeAlgorithm.getInstance().getRam()))) {
				location++;
			}
			//put 0 in locationResponseTimeLogTable
//			ArrayList lst=new ArrayList<>();
			for(int respTempVar=1;respTempVar<=HoneyBeeConstants.getInstance().getNoOfLocations();respTempVar++){
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
				if(!(HoneyBeeConstants.getInstance().isCPUAvailable(location, HoneyBeeAlgorithm.getInstance().getCpu()) && HoneyBeeConstants.getInstance().isHDAvailable(location, HoneyBeeAlgorithm.getInstance().getHd()) && HoneyBeeConstants.getInstance().isRAMAvailable(location, HoneyBeeAlgorithm.getInstance().getRam()))) {
					location++;
				}
				//put 0 in locationAverageResponseTimeLogTable
				for(int respAvgTempVar=1;respAvgTempVar<=HoneyBeeConstants.getInstance().getNoOfLocations();respAvgTempVar++){
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
			
			return location;
		}
		else{
			System.out.println("locationResponseTimeLogTable is not empty...");
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) { //print sysouts in below loops to find the BUG
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
					if(!insufficientResourceLocation.isEmpty()){
						if(!insufficientResourceLocation.contains(pair.getKey())){
							System.out.println("@@@@@@@@@@other than insufficient location data....");
							tempHM=pair.getValue();
							if(temp==0){
								tempMinAvgVal=Double.parseDouble(tempHM.get(requestType).get(0).toString());
								location=pair.getKey();
								temp++;
							}
							else{
								if(Double.parseDouble(tempHM.get(requestType).get(0).toString())<tempMinAvgVal){
									location=pair.getKey();
								}
							}
						}
					}
					else{
						System.out.println("locationAverageResponseTimeLogTable "+locationAverageResponseTimeLogTable);
						HashMap<Double, Integer> sampleHM=new HashMap<Double,Integer>();
						for(int val=1;val<=HoneyBeeConstants.getInstance().getNoOfLocations();val++){
							System.out.println("average response time for location "+val+"  "+locationAverageResponseTimeLogTable.get(val).get(requestType).get(0));
							sampleHM.put(Double.parseDouble(locationAverageResponseTimeLogTable.get(val).get(requestType).get(0).toString()), val);
						}
						TreeMap<Double, Integer> treeMap = new TreeMap<Double, Integer>(sampleHM);
						location=treeMap.get(treeMap.firstKey());
						return location;
					}
				
			}
			System.out.println("returning location "+location +" ...");
			return location;
		}
		
	}
	
	
	/*public void increaseFitnessValueOfLocation(int location) {
		
		double p = (fitnessTable.get(location) + AntConstants.getInstance().getDeltaPheromone()) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		fitnessTable.put(location, p);
	}
	
	
	public void decreaseFitnessValueOfLocation(int location) {
		
		double p = fitnessTable.get(location) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		fitnessTable.put(location, p);
	}*/
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	public void processFitnessValue(int location)throws Exception{
		
	}

	/**
	 * The is the actual logic / algorithm.
	 * @return
	 */
	
	public int honeybeeBasedControl(ArrayList<Integer> insufficientResourceLocation) {
		flag=false;
		// Fetch the location with highest fitness value.
		// This would be our candidate for processing request.
		int location = getLocationWithHighestFitnessValue(insufficientResourceLocation);
		System.out.println("@@@@@@@@@@@@@@");
		System.out.println("cpu required: "+HoneyBeeAlgorithm.getInstance().getCpu());
		
		/*//this is an extra check to  get out of recursion! by setting some value to tempCount
		if((HoneyBeeConstants.getInstance().isCPUAvailable(location, HoneyBeeAlgorithm.getInstance().getCpu()) && HoneyBeeConstants.getInstance().isHDAvailable(location, HoneyBeeAlgorithm.getInstance().getHd()) && HoneyBeeConstants.getInstance().isRAMAvailable(location, HoneyBeeAlgorithm.getInstance().getRam()))) {
			tempCount=-1;
		}*/
		
		
		// Check whether there are sufficient amount of resource available on that location.
		if(!(HoneyBeeConstants.getInstance().isCPUAvailable(location, HoneyBeeAlgorithm.getInstance().getCpu()) && HoneyBeeConstants.getInstance().isHDAvailable(location, HoneyBeeAlgorithm.getInstance().getHd()) && HoneyBeeConstants.getInstance().isRAMAvailable(location, HoneyBeeAlgorithm.getInstance().getRam()))) {
			//if sufficient resources not available then search for another free location
			insufficientResourceLocation.add(location);
			System.out.println("insufficient resources at location: "+insufficientResourceLocation);
			/*location = honeybeeBasedControl(insufficientResourceLocation);
			System.out.println("Searching for available resource server.......");*/
			if(tempCount<=HoneyBeeConstants.getInstance().getNoOfLocations()){
//				if(tempCount!=-1){
					location = honeybeeBasedControl(insufficientResourceLocation); //TODO this recursion is causing trouble due to its internal running
//					if(!insufficientResourceLocation.contains(location1) && flag==false/*tempCount>0*/){ //since tempCount++ is done after 'if' hence checked for value 1000 here
//						System.out.println("after location1:  "+location);
//						location=location1;
//						tempCount=-999;
//						flag=true;
//					}
					System.out.println("Searching for available resource server.......and tempCount is "+tempCount);
					tempCount++;
//				}
			}
			
			//this condition is commented since it is ASSUMED that all the servers will never run out of resources to serve the request
			/*if(tempCount>=HoneyBeeConstants.getInstance().getNoOfLocations()){
				location=-1;
				System.out.println("flushing insufficientResourceLocation...");
				insufficientResourceLocation.clear();
				tempCount=0;
				try {
					System.out.println("waiting for server to get free....");
					Thread.sleep(1000);
					location = honeybeeBasedControl(insufficientResourceLocation);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		}
		/*System.out.println("tempCount: "+tempCount+" and flag: "+flag);
		if(tempCount==1 || flag==true){
			//reduce the allocated resource
			HoneyBeeConstants.getInstance().increaseLocationDetails(location, HoneyBeeAlgorithm.getInstance().getCpu(), HoneyBeeAlgorithm.getInstance().getHd(), HoneyBeeAlgorithm.getInstance().getRam());
			System.out.println("Location having highest Fitness: " + location);
			flag=false;
		}*/
		
		return location;
	}
	
}
