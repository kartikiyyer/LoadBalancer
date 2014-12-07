package algorithm.location;
import java.text.SimpleDateFormat;
import java.util.*;

public class LocationAwareAlgorithm
{

	public HashMap<Integer, Double> currentCPUState;
	public HashMap<Integer, Double> currentStorageState;
	public HashMap<Integer, Double> currentRAMState;
	HashMap<Integer, Integer> currentNoOfRequests;
	int numOfVm;
	private static LocationAwareAlgorithm locationAwareAlgorithm;
	public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
	int location;
	int request;
	
	public HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationAverageResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	private HashMap<Integer, Double[]> reqCost = new HashMap<Integer,Double[]>();
	
	public HashMap<Integer, Double[]> getReqCost() {
		return reqCost;
	}

	public void setReqCost(HashMap<Integer, Double[]> reqCost) {
		this.reqCost = reqCost;
	}
	
	public int getRequest() {
		return request;
	}


	public void setRequest(int request) {
		this.request = request;
	}
	
	private LocationAwareAlgorithm() {
		currentCPUState = new HashMap<Integer,Double>();
		currentStorageState = new HashMap<Integer,Double>();
		currentRAMState = new HashMap<Integer,Double>();
		numOfVm = LocationAwareConstants.getInstance().getNoOfLocations();
		for(int i=1;i<= LocationAwareConstants.getInstance().getNoOfLocations();i++)
			insertIntoReferenceTable(i, LocationAwareConstants.getInstance().getLocationMaxCPU().get(i), LocationAwareConstants.getInstance().getLocationMaxHD().get(i),LocationAwareConstants.getInstance().getLocationMaxRAM().get(i));
	}



	public static synchronized LocationAwareAlgorithm getInstance() {
		if(locationAwareAlgorithm == null) {
			locationAwareAlgorithm = new LocationAwareAlgorithm();
		}
		return locationAwareAlgorithm;
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
	
//	public int runLocationAwareAlgorithm(Double[] inputLocation, double cpu, double storage, double ram) {
	public int runLocationAwareAlgorithm(Double[] inputLocation,int zone, double cpu, double storage, double ram) {
		int tempDistance = 0;
		int tempLocation = 0;
		HashMap<Integer, Double[]> tempMap = new HashMap<Integer,Double[]>();

		//changes - new code
		
		HashMap<Integer, int[]> tempZoneWiseLocNo = LocationAwareConstants.getInstance().getZoneLocations();
		int[] currVal = tempZoneWiseLocNo.get(zone);
		
		for(int i=currVal[0];i<currVal[0]+currVal.length;i++)
		{
			if(cpu < currentCPUState.get(i) && storage < currentStorageState.get(i) && ram <currentRAMState.get(i))
			{
				tempMap.put(i, LocationAwareConstants.getInstance().geoLocation.get(i));
				
//				System.out.println("In runLocationAwareAlgorithm: TempLocation<key>:"+i+" <Lattitude>:"+LocationAwareConstants.getInstance().geoLocation.get(i)[0]+" <Longitude>:"+LocationAwareConstants.getInstance().geoLocation.get(i)[1]);
			}

		}

		Iterator it = tempMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			//System.out.println(pairs.getKey() + " = " + pairs.getValue());
			Double [] b = (Double[]) pairs.getValue();
			System.out.println(b[0] + ""  + b[1]);
			int distance = calculateDistance(b[0],b[1],inputLocation[0],inputLocation[1]);
			System.out.println(" distance from server : " + distance );
			if (tempDistance == 0){
				tempDistance = distance;	
				tempLocation = (int) pairs.getKey();
			}
			else if (tempDistance >= distance){
				tempDistance = distance;
				System.out.println("Temp Distance:"+tempDistance+" Distance "+distance);
				tempLocation = (int) pairs.getKey();
				System.out.println("tempLocation:"+tempLocation);
			}
		}
		location = tempLocation;
		
		
		
		if(locationResponseTimeLogTable.isEmpty()){
			System.out.println("locationResponseTimeLogTable is empty..");
			//put 0 in locationResponseTimeLogTable
//			ArrayList lst=new ArrayList<>();
			for(int respTempVar=1;respTempVar<=LocationAwareConstants.getInstance().getNoOfLocations();respTempVar++){
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
				for(int respAvgTempVar=1;respAvgTempVar<=LocationAwareConstants.getInstance().getNoOfLocations();respAvgTempVar++){
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
		
		
		updateReferenceTable(location, cpu, storage, ram);
		return location;
	}

	public void decreaseLocationDetails(int location, double currentCPUState, double currentStorageState, double currentRAMState) {
		System.out.println("In Decrease Location Details");
		this.currentCPUState.put(location, this.currentCPUState.get(location) + currentCPUState);
		this.currentStorageState.put(location, this.currentStorageState.get(location) + currentStorageState);
		this.currentRAMState.put(location, this.currentRAMState.get(location) + currentRAMState);
	}

	public void insertIntoReferenceTable(int vm,double cpu,double hd,double ram)
	{
		System.out.println("insertIntoReferenceTable....CPU<"+cpu+"> hd<"+hd+"> ram<"+ram+">");
		currentCPUState.put(vm, cpu);
		currentStorageState.put(vm, hd);
		currentRAMState.put(vm, ram);
	}

	public void updateReferenceTable(int vm,double cpu,double hd,double ram)
	{
		System.out.println("Reference Table Updated After Request Allocated.."+vm+" cpu"+cpu);
		System.out.println("Before Updating: "+currentCPUState.get(vm)+" "+currentCPUState.get(vm)+" "+currentCPUState.get(vm));
		currentCPUState.put(vm, currentCPUState.get(vm)- cpu);
		currentStorageState.put(vm, currentStorageState.get(vm)-hd);
		currentRAMState.put(vm, currentRAMState.get(vm)- ram);
		System.out.println("After Updating: "+currentCPUState.get(vm)+" "+currentCPUState.get(vm)+" "+currentCPUState.get(vm));

	}
	public int calculateDistance(double userLat, double userLng,
			double venueLat, double venueLng) {

		double latDistance = Math.toRadians(userLat - venueLat);
		double lngDistance = Math.toRadians(userLng - venueLng);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
				* Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
	}

}


