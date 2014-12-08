package algorithm.ant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AntConstants {
	private int noOfLocations;
	private double pheromone = 0.5;
	private double deltaPheromone;
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	private String server = "ubuntu-mc-1-cmpe281.cloudapp.net";
	//private String server = "localhost";
	private HashMap<Integer, Integer> locationRequestCount = new HashMap<Integer, Integer>();
	
	private HashMap<Integer, Double> locationCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationRAM = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	private static AntConstants antConstants;
	
	public static HashMap<Integer, Double[]> locationCost = new HashMap<Integer, Double[]>();
	public static HashMap<Integer, Double[]> costHM = new HashMap<Integer, Double[]>();
	
	//{location->[cpu,hd,ram]}
	public static HashMap<Integer, Double[]> currentUsage = new HashMap<Integer, Double[]>();
	
	private HashMap<Integer, ArrayList<Integer>> zones = new HashMap<Integer, ArrayList<Integer>>();
	
	private AntConstants() {
		
		zones.put(1, new ArrayList<Integer>(Arrays.asList(1,2,3)));
		zones.put(2, new ArrayList<Integer>(Arrays.asList(4,5,6)));
		zones.put(3, new ArrayList<Integer>(Arrays.asList(7,8,9)));

		Double[] cost = new Double[3];
		cost[0]=0.018;
		cost[1]=0.003;
		cost[2]=0.005;
		locationCost.put(1, cost);
		
		cost = new Double[3];
		cost[0]=0.044;
		cost[1]=0.006;
		cost[2]=0.008;
		locationCost.put(2, cost);
		
		cost = new Double[3];
		cost[0]=0.088;
		cost[1]=0.010;
		cost[2]=0.029;
		locationCost.put(3, cost);
		
		cost = new Double[3];
		cost[0]=0.018;
		cost[1]=0.003;
		cost[2]=0.005;
		locationCost.put(4, cost);
		
		cost = new Double[3];
		cost[0]=0.044;
		cost[1]=0.006;
		cost[2]=0.008;
		locationCost.put(5, cost);
	
		cost = new Double[3];
		cost[0]=0.088;
		cost[1]=0.010;
		cost[2]=0.029;
		locationCost.put(6, cost);
		
		cost = new Double[3];
		cost[0]=0.018;
		cost[1]=0.003;
		cost[2]=0.005;
		locationCost.put(7, cost);
		
		cost = new Double[3];
		cost[0]=0.044;
		cost[1]=0.006;
		cost[2]=0.008;
		locationCost.put(8, cost);
		
		cost = new Double[3];
		cost[0]=0.088;
		cost[1]=0.010;
		cost[2]=0.029;
		locationCost.put(9, cost);
		
		locations.put(1, "ubuntu-mc-1-server.cloudapp.net");
		locations.put(2, "ubuntu-mc-2-server.cloudapp.net");
		locations.put(3, "ubuntu-mc-3-server.cloudapp.net");
		locations.put(4, "ubuntu-mc-4-server.cloudapp.net");
		locations.put(5, "ubuntu-mc-5-server.cloudapp.net");
		locations.put(6, "ubuntu-mc-1-cmpe281.cloudapp.net");
		locations.put(7, "ubuntu-mc-7-server.cloudapp.net");
		locations.put(8, "ubuntu-mc-8-server.cloudapp.net");
		locations.put(9, "ubuntu-mc-9-server.cloudapp.net");
		
		
		
		locationRequestCount.put(1, 0);
		locationRequestCount.put(2, 0);
		locationRequestCount.put(3, 0);
		locationRequestCount.put(4, 0);
		locationRequestCount.put(5, 0);
		locationRequestCount.put(6, 0);
		locationRequestCount.put(7, 0);
		locationRequestCount.put(8, 0);
		locationRequestCount.put(9, 0);
		
		
		noOfLocations = locations.size();
		
		deltaPheromone = 1.0 / noOfLocations;
		
		// Initialize configuration of each machine.
		locationCPU.put(1, 0.3);
		locationCPU.put(2, 0.3);
		locationCPU.put(3, 0.3);
		locationCPU.put(4, 0.3);
		locationCPU.put(5, 0.3);	
		locationCPU.put(6, 0.3);
		locationCPU.put(7, 0.3);
		locationCPU.put(8, 0.3);
		locationCPU.put(9, 0.3);	
		
		
		locationHD.put(1, 10.0);
		locationHD.put(2, 10.0);
		locationHD.put(3, 10.0);
		locationHD.put(4, 10.0);
		locationHD.put(5, 10.0);	
		locationHD.put(6, 10.0);
		locationHD.put(7, 10.0);
		locationHD.put(8, 10.0);
		locationHD.put(9, 10.0);	
		
		
		locationRAM.put(1, 10.0);
		locationRAM.put(2, 10.0);
		locationRAM.put(3, 10.0);
		locationRAM.put(4, 10.0);
		locationRAM.put(5, 10.0);	
		locationRAM.put(6, 10.0);
		locationRAM.put(7, 10.0);
		locationRAM.put(8, 10.0);
		locationRAM.put(9, 10.0);	
		
		
		
		// Initialize max configuration of each machine that it can hold.
		locationMaxCPU.put(1, 2.0);
		locationMaxCPU.put(2, 2.0);
		locationMaxCPU.put(3, 2.0);
		locationMaxCPU.put(4, 2.0);
		locationMaxCPU.put(5, 2.0);	
		locationMaxCPU.put(6, 2.0);
		locationMaxCPU.put(7, 2.0);
		locationMaxCPU.put(8, 2.0);
		locationMaxCPU.put(9, 2.0);
		
		
		locationMaxHD.put(1, 300.0);
		locationMaxHD.put(2, 300.0);
		locationMaxHD.put(3, 300.0);
		locationMaxHD.put(4, 300.0);
		locationMaxHD.put(5, 300.0);	
		locationMaxHD.put(6, 300.0);
		locationMaxHD.put(7, 300.0);
		locationMaxHD.put(8, 300.0);
		locationMaxHD.put(9, 300.0);	
	
		locationMaxRAM.put(1, 500.0);
		locationMaxRAM.put(2, 500.0);
		locationMaxRAM.put(3, 500.0);
		locationMaxRAM.put(4, 500.0);
		locationMaxRAM.put(5, 500.0);
		locationMaxRAM.put(6, 500.0);
		locationMaxRAM.put(7, 500.0);
		locationMaxRAM.put(8, 500.0);
		locationMaxRAM.put(9, 500.0);

	}
	
	public static synchronized AntConstants getInstance() {
		if(antConstants == null) {
			antConstants = new AntConstants();
		}
		return antConstants;
	}
	
	public int getNoOfLocations() {
		return noOfLocations;
	}

	public double getPheromone() {
		return pheromone;
	}

	public double getDeltaPheromone() {
		return deltaPheromone;
	}

	public HashMap<Integer, String> getLocations() {
		return locations;
	}

	public HashMap<Integer, Double> getLocationCPU() {
		return locationCPU;
	}

	public HashMap<Integer, Double> getLocationHD() {
		return locationHD;
	}

	public HashMap<Integer, Double> getLocationRAM() {
		return locationRAM;
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

	public HashMap<Integer, ArrayList<Integer>> getZones() {
		return zones;
	}

	public void initializeLocationDetails(HashMap<Integer, Double> locationCPU, HashMap<Integer, Double> locationHD, HashMap<Integer, Double> locationRAM,
			HashMap<Integer, Double> locationMaxCPU, HashMap<Integer, Double> locationMaxHD, HashMap<Integer, Double> locationMaxRAM) {
		this.locationCPU = locationCPU;
		this.locationHD = locationHD;
		this.locationRAM = locationRAM;
		this.locationMaxCPU = locationMaxCPU;
		this.locationMaxHD = locationMaxHD;
		this.locationMaxRAM = locationMaxRAM;
		
	}
	
	public void increaseLocationDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		this.locationCPU.put(location, this.locationCPU.get(location) + locationCPU);
		this.locationHD.put(location, this.locationHD.get(location) + locationHD);
		this.locationRAM.put(location, this.locationRAM.get(location) + locationRAM);
		
		Double[] currUsage=new Double[3];
		currUsage[0]=this.locationCPU.get(location);
		currUsage[1]=this.locationHD.get(location);
		currUsage[2]=this.locationRAM.get(location);
		currentUsage.put(location, currUsage);
	}
	
	public void decreaseLocationDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		this.locationCPU.put(location, this.locationCPU.get(location) - locationCPU);
		this.locationHD.put(location, this.locationHD.get(location) - locationHD);
		this.locationRAM.put(location, this.locationRAM.get(location) - locationRAM);
		
		Double[] currUsage=new Double[3];
		currUsage[0]=this.locationCPU.get(location);
		currUsage[1]=this.locationHD.get(location);
		currUsage[2]=this.locationRAM.get(location);
		currentUsage.put(location, currUsage);
	}
	
	public void increaseLocationMaxDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		this.locationMaxCPU.put(location, this.locationMaxCPU.get(location) + locationCPU);
		this.locationMaxHD.put(location, this.locationMaxHD.get(location) + locationHD);
		this.locationMaxRAM.put(location, this.locationMaxRAM.get(location) + locationRAM);
	}
	
	public void decreaseLocationMaxDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		this.locationMaxCPU.put(location, this.locationMaxCPU.get(location) - locationCPU);
		this.locationMaxHD.put(location, this.locationMaxHD.get(location) - locationHD);
		this.locationMaxRAM.put(location, this.locationMaxRAM.get(location) - locationRAM);
	}
	
	public boolean isCPUAvailable(int location, double locationCPU) {
		if(this.locationMaxCPU.get(location) >= (this.locationCPU.get(location) + locationCPU)) {
			return true;
		}
		return false;
	}
	
	public boolean isHDAvailable(int location, double locationHD) {
		if(this.locationMaxHD.get(location) >= (this.locationHD.get(location) + locationHD)) {
			return true;
		}
		return false;
	}
	
	public boolean isRAMAvailable(int location, double locationRAM) {
		if(this.locationMaxRAM.get(location) >= (this.locationRAM.get(location) + locationRAM)) {
			return true;
		}
		return false;
	}
	
	public void increaseLocationRequestCount(int location) {
		locationRequestCount.put(location, locationRequestCount.get(location) + 1);
	}
	
	public void decreaseLocationRequestCount(int location) {
		locationRequestCount.put(location, locationRequestCount.get(location) - 1);
	}
	
}
