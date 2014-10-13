package algorithm.honeybee;

import java.util.HashMap;

public class HoneyBeeConstants {


	private int noOfLocations;
	private double fitness = 5;
//	private double deltaPheromone;
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	private String server = "ubuntu-mc-serve.cloudapp.net";
	private HashMap<Integer, Integer> locationRequestCount = new HashMap<Integer, Integer>();
	
	private HashMap<Integer, Double> locationCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationRAM = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	private static HoneyBeeConstants honeybeeConstants;
	
	private HoneyBeeConstants() {
		
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		//locations.put(3, "ubuntu-mc-3.cloudapp.net");
		//locations.put(4, "ubuntu-mc-4.cloudapp.net");
		//locations.put(5, "ubuntu-mc-5.cloudapp.net");		
		
		
		locationRequestCount.put(1, 0);
		locationRequestCount.put(2, 0);
		locationRequestCount.put(3, 0);
		locationRequestCount.put(4, 0);
		locationRequestCount.put(5, 0);
		
		noOfLocations = locations.size();
		
//		deltaPheromone = 1.0 / noOfLocations;
		
		// Initialize configuration of each machine.
		locationCPU.put(1, 0.3);
		locationCPU.put(2, 0.3);
		locationCPU.put(3, 0.3);
		locationCPU.put(4, 0.3);
		locationCPU.put(5, 0.3);	
		
		locationHD.put(1, 10.0);
		locationHD.put(2, 10.0);
		locationHD.put(3, 10.0);
		locationHD.put(4, 10.0);
		locationHD.put(5, 10.0);	
		
		locationRAM.put(1, 10.0);
		locationRAM.put(2, 10.0);
		locationRAM.put(3, 10.0);
		locationRAM.put(4, 10.0);
		locationRAM.put(5, 10.0);	
		
		// Initialize max configuration of each machine that it can hold.
		locationMaxCPU.put(1, 2.0);
		locationMaxCPU.put(2, 2.0);
		locationMaxCPU.put(3, 2.0);
		locationMaxCPU.put(4, 2.0);
		locationMaxCPU.put(5, 2.0);	
		
		locationMaxHD.put(1, 300.0);
		locationMaxHD.put(2, 300.0);
		locationMaxHD.put(3, 300.0);
		locationMaxHD.put(4, 300.0);
		locationMaxHD.put(5, 300.0);	
		
		locationMaxRAM.put(1, 500.0);
		locationMaxRAM.put(2, 500.0);
		locationMaxRAM.put(3, 500.0);
		locationMaxRAM.put(4, 500.0);
		locationMaxRAM.put(5, 500.0);
	}
	
	public static synchronized HoneyBeeConstants getInstance() {
		if(honeybeeConstants == null) {
			honeybeeConstants = new HoneyBeeConstants();
		}
		return honeybeeConstants;
	}
	
	public int getNoOfLocations() {
		return noOfLocations;
	}

	public double getFitness() {
		return fitness;
	}

	/*public double getDeltaPheromone() {
		return deltaPheromone;
	}*/

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
	}
	
	public void decreaseLocationDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		this.locationCPU.put(location, this.locationCPU.get(location) - locationCPU);
		this.locationHD.put(location, this.locationHD.get(location) - locationHD);
		this.locationRAM.put(location, this.locationRAM.get(location) - locationRAM);
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
