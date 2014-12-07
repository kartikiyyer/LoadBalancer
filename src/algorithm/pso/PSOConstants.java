package algorithm.pso;

import java.util.HashMap;

public class PSOConstants {
	private int noOfLocations;
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	private String server = "ubuntu-mc-server";
	private HashMap<Integer, Integer> locationRequestCount = new HashMap<Integer, Integer>();

	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	private static PSOConstants PSOConstants;
	
	public static HashMap<Integer, Double[]> locationCost = new HashMap<Integer, Double[]>();
	public static HashMap<Integer, Double[]> costHM = new HashMap<Integer, Double[]>();
	
	private PSOConstants() {
		
		
		Double[] cost = new Double[3];
		cost[0]=0.0001;
		cost[1]=0.00008;
		cost[2]=0.00019;
		locationCost.put(1, cost);
		
		cost = new Double[3];
		cost[0]=0.0002;
		cost[1]=0.0001;
		cost[2]=0.00015;
		locationCost.put(2, cost);
		
		cost = new Double[3];
		cost[0]=0.0001;
		cost[1]=0.00008;
		cost[2]=0.00029;
		locationCost.put(3, cost);
		
		cost = new Double[3];
		cost[0]=0.0002;
		cost[1]=0.0001;
		cost[2]=0.00015;
		locationCost.put(4, cost);
		
		cost = new Double[3];
		cost[0]=0.00015;
		cost[1]=0.00019;
		cost[2]=0.00019;
		locationCost.put(5, cost);
	
		cost = new Double[3];
		cost[0]=0.00019;
		cost[1]=0.00016;
		cost[2]=0.0002;
		locationCost.put(6, cost);
		
		cost = new Double[3];
		cost[0]=0.0002;
		cost[1]=0.0001;
		cost[2]=0.00029;
		locationCost.put(7, cost);
		
		cost = new Double[3];
		cost[0]=0.0002;
		cost[1]=0.00008;
		cost[2]=0.00016;
		locationCost.put(8, cost);
		
		cost = new Double[3];
		cost[0]=0.0001;
		cost[1]=0.0001;
		cost[2]=0.00015;
		locationCost.put(9, cost);
		
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		locations.put(3, "ubuntu-mc-3.cloudapp.net");
		locations.put(4, "ubuntu-mc-4.cloudapp.net");
		//locations.put(5, "localhost");		
		
		
		locationRequestCount.put(1, 0);
		locationRequestCount.put(2, 0);
		locationRequestCount.put(3, 0);
		locationRequestCount.put(4, 0);
		//locationRequestCount.put(5, 0);
		
		noOfLocations = locations.size();
	
		
		// Initialize configuration of each machine.
		
		
		// Initialize max configuration for each machine
		locationMaxCPU.put(1, 2.0);
		locationMaxCPU.put(2, 2.0);
		locationMaxCPU.put(3, 2.0);
		locationMaxCPU.put(4, 2.0);
		//locationMaxCPU.put(5, 2.0);	
		
		locationMaxHD.put(1, 300.0);
		locationMaxHD.put(2, 300.0);
		locationMaxHD.put(3, 300.0);
		locationMaxHD.put(4, 300.0);
		//locationMaxHD.put(5, 300.0);	
		
		locationMaxRAM.put(1, 500.0);
		locationMaxRAM.put(2, 500.0);
		locationMaxRAM.put(3, 500.0);
		locationMaxRAM.put(4, 500.0);
		//locationMaxRAM.put(5, 500.0);
	}
	
	public static synchronized PSOConstants getInstance() {
		if(PSOConstants == null) {
			PSOConstants = new PSOConstants();
		}
		return PSOConstants;
	}
	
	public int getNoOfLocations() {
		return noOfLocations;
	}


	public HashMap<Integer, String> getLocations() {
		return locations;
	}

	

	public HashMap<Integer, Double> getLocationMaxCPU() {
		return locationMaxCPU;
	}

	public HashMap<Integer, Double> getLocationMaxHD() {
		return locationMaxHD;
	}

	public HashMap<Integer, Double> getLocationMaxRAM() {
		return locationMaxRAM;
	}

	public String getServer() {
		return server;
	}

	public void initializeLocationDetails(HashMap<Integer, Double> locationCPU, HashMap<Integer, Double> locationHD, HashMap<Integer, Double> locationRAM,
			HashMap<Integer, Double> locationMaxCPU, HashMap<Integer, Double> locationMaxHD, HashMap<Integer, Double> locationMaxRAM) {
		PSOAlgorithm.getInstance().currentCPUState = locationCPU;
		PSOAlgorithm.getInstance().currentStorageState = locationHD;
		PSOAlgorithm.getInstance().currentRAMState = locationRAM;
		this.locationMaxCPU = locationMaxCPU;
		this.locationMaxHD = locationMaxHD;
		this.locationMaxRAM = locationMaxRAM;
		
	}
	
	public void increaseLocationRequestCount(int location) {
		locationRequestCount.put(location, locationRequestCount.get(location) + 1);
	}
	
}
