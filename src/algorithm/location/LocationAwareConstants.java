package algorithm.location;

import java.util.HashMap;

import algorithm.pso.PSOAlgorithm;
import algorithm.pso.PSOConstants;

public class LocationAwareConstants {
	HashMap<Integer, Double[]> geoLocation = new HashMap<Integer,Double[]>();
	Double laLn1[] = new Double[]{32.7157380,-117.1610840};
	Double laLn2[] = new Double[]{40.712784,-74.005941};
	Double laLn3[] = new Double[]{43.712784,-54.005941};
	Double laLn4[] = new Double[]{52.712784,-84.005941};
	
	private int noOfLocations;
	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	//Double laLn2[] = new Double[]{37.3393860,-121.8949550};
	private static LocationAwareConstants locationAwareConstants;
	private LocationAwareConstants()
	{
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		locations.put(3, "ubuntu-mc-3.cloudapp.net");
		locations.put(4, "ubuntu-mc-4.cloudapp.net");
		
		geoLocation.put(1,laLn1);
		geoLocation.put(2,laLn2);
		geoLocation.put(3,laLn3);
		geoLocation.put(4,laLn4);
		
		noOfLocations = locations.size();
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
		//geoLocation.put(3,laLn2);
	}
	

	public static synchronized LocationAwareConstants getInstance() {
		if(locationAwareConstants == null) {
			locationAwareConstants = new LocationAwareConstants();
		}
		return locationAwareConstants;
	}

	public HashMap<Integer, String> getLocations() {
		return locations;
	}
	
	public int getNoOfLocations() {
		return noOfLocations;
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
	
	public void initializeLocationDetails(HashMap<Integer, Double> locationCPU, HashMap<Integer, Double> locationHD, HashMap<Integer, Double> locationRAM,
			HashMap<Integer, Double> locationMaxCPU, HashMap<Integer, Double> locationMaxHD, HashMap<Integer, Double> locationMaxRAM) {
		LocationAwareAlgorithm.getInstance().currentCPUState = locationCPU;
		LocationAwareAlgorithm.getInstance().currentStorageState = locationHD;
		LocationAwareAlgorithm.getInstance().currentRAMState = locationRAM;
		this.locationMaxCPU = locationMaxCPU;
		this.locationMaxHD = locationMaxHD;
		this.locationMaxRAM = locationMaxRAM;
		
	}

}
