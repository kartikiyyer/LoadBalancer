package algorithm.pso;
import algorithm.ant.AntConstants;
import algorithm.pso.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PSOAlgorithm {


	/*
	 *Algorithm Steps
	 *1)Initialization
	 *  Randomly generate each particle's velocity and position
	 *  Repair infeasible positions into feasible positions
	 *  Evaluate the fitness value of each particle
	 *  Fill the pBest of each particle with its current position
	 *  Update the exemplar vector of each particle
	 *  
	 *2) Optimization
	 *	 Repeat
	 *    For each particle 
	 *     if particle ceases improving rg iterations 
	 *      update its exemplar vector;
	 *     end if
	 *      Update particle's velocity and position;
	 *     if particle position is feasible 
	 *      Evaluate the fitness value of the particle
	 *    	Update the pBest
	 *     end if
	 *     End for
	 */ 

	private double cpu;
	private double hd;
	private double ram;
	private static PSOAlgorithm PSOAlgorithm;
	int numOfVm;
	int range;
	int numOfParticles;
	ArrayList<Particle> particle;
	Random random;
	Swarm swarm;
	int req;
	int location;
	int request;
	public HashMap<Integer, Double> currentCPUState;
	public HashMap<Integer, Double> currentStorageState;
	public HashMap<Integer, Double> currentRAMState;
	HashMap<Integer, Integer> currentNoOfRequests;
	private HashMap<Integer, HashMap<Integer, List>> reqResTimeLogTable = new HashMap<Integer, HashMap<Integer, List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	public HashMap<Integer, HashMap<Integer,List>> locationAverageResponseTimeLogTable = new HashMap<Integer, HashMap<Integer,List>>();
	

	private PSOAlgorithm()
	{
		numOfVm = PSOConstants.getInstance().getNoOfLocations();
		range = 10;
		numOfParticles = range * numOfVm;
		random = new Random();
		particle = new ArrayList<Particle>();
		currentCPUState = new HashMap<Integer,Double>();
		currentStorageState = new HashMap<Integer,Double>();
		currentRAMState = new HashMap<Integer,Double>();
		currentNoOfRequests = new HashMap<Integer,Integer>();
		init();
		//init(double cpu, double storage, double ram, double time);
	}

	public static synchronized PSOAlgorithm getInstance() {
		if(PSOAlgorithm == null) {
			PSOAlgorithm = new PSOAlgorithm();
		}
		return PSOAlgorithm;
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
	
	public int runPSOAlgorithm(double cpu, double storage, double ram, double time,int request)
	{
		location = randomInt(0, numOfVm);
		System.out.println("In runPSOAlgorithm");

		optimize(cpu,storage,ram,time);


		return location;
	}

	public double randomDouble(double min, double max) {

		double randomDoubleValue = min + (max - min) * random.nextDouble();
		return randomDoubleValue;
	}

	public int randomInt(int min, int max) {

		int randomIntValue = random.nextInt((max - min) + 1) + min;
		return randomIntValue;
	}

	public void evaluateFitness(Particle p)
	{

		//System.out.println("Evaluating fitness for Particle<"+p.getIndex()+">");
		int loc = location;
		p.setpBest(loc);
	}

	public void updateExemplarVecor(Particle p)
	{
		//System.out.println("Updating Exemplar Vector.....");
		p.setPosition(randomInt(1, 10));
		p.setVelocity(randomInt(1, 10));
		p.setExemplarVector(p.getVelocity());
	}

	public void insertIntoBlackBoard(int vm,double cpu,double hd,double ram)
	{
		System.out.println("InsertIntoBlackBoard....CPU<"+cpu+"> hd<"+hd+"> ram<"+ram+">");
		currentCPUState.put(vm, cpu);
		currentStorageState.put(vm, hd);
		currentRAMState.put(vm, ram);
	}

	public void updateBlackBoard(int vm,double cpu,double hd,double ram)
	{
		System.out.println("BlackBoard Updated After Request Allocated.."+vm+" cpu"+cpu);
		System.out.println("Before Updating: "+currentCPUState.get(vm)+" "+currentCPUState.get(vm)+" "+currentCPUState.get(vm));
		currentCPUState.put(vm, currentCPUState.get(vm)- cpu);
		currentStorageState.put(vm, currentStorageState.get(vm)-hd);
		currentRAMState.put(vm, currentRAMState.get(vm)- ram);
		System.out.println("After Updating: "+currentCPUState.get(vm)+" "+currentCPUState.get(vm)+" "+currentCPUState.get(vm));
	
	}
	
	public void decreaseLocationDetails(int location, double currentCPUState, double currentStorageState, double currentRAMState) {
		this.currentCPUState.put(location, this.currentCPUState.get(location) + currentCPUState);
		this.currentStorageState.put(location, this.currentStorageState.get(location) + currentStorageState);
		this.currentRAMState.put(location, this.currentRAMState.get(location) + currentRAMState);
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
	
	public void init()
	{
		System.out.println("Init Called");
		double[] resource = new double[3];
		int j = 0;
		int n=1;
		req = 0;
		
		//int countOfVm = 0;
		for(int i=1; i<=numOfVm; i++)
		{
			currentNoOfRequests.put(i,0);
			insertIntoBlackBoard(i, PSOConstants.getInstance().getLocationMaxCPU().get(i), PSOConstants.getInstance().getLocationMaxHD().get(i),PSOConstants.getInstance().getLocationMaxRAM().get(i));
			for(int k=0;k<range;k++)
			{
				j=0;
				resource[j] = randomDouble(0.0,PSOConstants.getInstance().getLocationMaxCPU().get(i));
				resource[j+1] = randomDouble(0.0,PSOConstants.getInstance().getLocationMaxHD().get(i));
				resource[j+2] = randomDouble(0.0,PSOConstants.getInstance().getLocationMaxRAM().get(i));
				particle.add(new Particle(resource, randomInt(0, 20), randomInt(0, 20), i,n));

				//System.out.println("Particle < "+n+" >"+" : "+"<CPU>:"+resource[j]+"| <HD>:"+resource[j+1]+"| <RAM>"+resource[j+2]);
				n++;
			}
		}


			for(Particle p : particle)
		{
			evaluateFitness(p);
			updateExemplarVecor(p);
		}

		swarm = new Swarm(particle);
		swarm.setgBest(1);

	}

	public void optimize(double cpu, double storage, double ram, double time)
	{
		//int[] tempArr;
		System.out.println("In Optimize.....");
		ArrayList<Integer> tempArr = new ArrayList<Integer>();
		for(int i=1;i<=numOfVm;i++)
		{
			if(cpu < currentCPUState.get(i) && storage < currentStorageState.get(i) && ram <currentRAMState.get(i))
			{
				tempArr.add(i);
			}

		}
		
		location = tempArr.get(random.nextInt(tempArr.size()));

		
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
		
		
		for(Particle p : particle)
		{
			evaluateFitness(p);
			updateExemplarVecor(p);
		}
		System.out.println("Location:"+location);
		updateBlackBoard(location, cpu, storage, ram);
	}
	


}
