package algorithm.location;

import java.util.HashMap;

import algorithm.pso.PSOAlgorithm;
import algorithm.pso.PSOConstants;

public class LocationAwareConstants {
	
	/*HashMap<Integer, Double[]> geoLocation = new HashMap<Integer,Double[]>();
	Double laLn1[] = new Double[]{32.7157380,-117.1610840};
	Double laLn2[] = new Double[]{40.712784,-74.005941};
	Double laLn3[] = new Double[]{43.712784,-54.005941};
	Double laLn4[] = new Double[]{52.712784,-84.005941};
	
	private int noOfLocations;
	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();*/
	
	//changes - new code

	Double laLn1[] = new Double[]{31.7157380,-120.1610840};
	Double laLn2[] = new Double[]{28.712784,-110.005941};
	Double laLn3[] = new Double[]{34.712784,-100.005941};
	
	Double laLn4[] = new Double[]{45.712784,-75.005941};
	Double laLn5[] = new Double[]{41.7157380,-69.1610840};
	Double laLn6[] = new Double[]{38.712784,-84.005941};
	
	Double laLn7[] = new Double[]{40.712784,-54.005941};
	Double laLn8[] = new Double[]{39.712784,-58.005941};
	Double laLn9[] = new Double[]{49.712784,-64.005941};
	
	public HashMap<Integer, Double[]> geoLocation = new HashMap<Integer,Double[]>();
	
	private int noOfLocations;
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	private String server = "ubuntu-mc-serve.cloudapp.net";
	private HashMap<Integer, Integer> locationRequestCount = new HashMap<Integer, Integer>();
	
	private HashMap<Integer, Double> locationCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationRAM = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	
	private HashMap<Integer,Integer> noOfLocationsWrtZone = new HashMap<Integer,Integer>();
	private HashMap<Integer, int[]> zoneLocations = new HashMap<Integer, int[]>();
	
	public static HashMap<Integer, Double[]> locationCost = new HashMap<Integer, Double[]>();
	public static HashMap<Integer, Double> costHM = new HashMap<Integer, Double>();
	
	//Double laLn2[] = new Double[]{37.3393860,-121.8949550};
	private static LocationAwareConstants locationAwareConstants;
	private LocationAwareConstants()
	{
		/*locations.put(1, "ubuntu-mc-1.cloudapp.net");
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
*/	
		
		
		Double[] cost = new Double[3];
		cost[0]=0.1;
		cost[1]=0.08;
		cost[2]=0.19;
		locationCost.put(1, cost);
		
		cost = new Double[3];
		cost[0]=0.2;
		cost[1]=0.1;
		cost[2]=0.15;
		locationCost.put(2, cost);
		
		cost = new Double[3];
		cost[0]=0.1;
		cost[1]=0.08;
		cost[2]=0.29;
		locationCost.put(3, cost);
		
		cost = new Double[3];
		cost[0]=0.2;
		cost[1]=0.1;
		cost[2]=0.15;
		locationCost.put(4, cost);
		
		cost = new Double[3];
		cost[0]=0.15;
		cost[1]=0.19;
		cost[2]=0.19;
		locationCost.put(5, cost);
	
		cost = new Double[3];
		cost[0]=0.19;
		cost[1]=0.16;
		cost[2]=0.2;
		locationCost.put(6, cost);
		
		cost = new Double[3];
		cost[0]=0.2;
		cost[1]=0.1;
		cost[2]=0.29;
		locationCost.put(7, cost);
		
		cost = new Double[3];
		cost[0]=0.2;
		cost[1]=0.08;
		cost[2]=0.16;
		locationCost.put(8, cost);
		
		cost = new Double[3];
		cost[0]=0.1;
		cost[1]=0.1;
		cost[2]=0.15;
		locationCost.put(9, cost);

		
		geoLocation.put(1,laLn1);
		geoLocation.put(2,laLn2);
		geoLocation.put(3,laLn3);
		geoLocation.put(4,laLn4);
		geoLocation.put(5,laLn5);
		geoLocation.put(6,laLn6);
		geoLocation.put(7,laLn7);
		geoLocation.put(8,laLn8);
		geoLocation.put(9,laLn9);
		
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		locations.put(3, "ubuntu-mc-3.cloudapp.net");
		int[] locArr=new int[] {1,2,3}; 
		zoneLocations.put(1, locArr);
		noOfLocationsWrtZone.put(1, locArr.length);
		
		locations.put(4, "ubuntu-mc-4.cloudapp.net");
		locations.put(5, "ubuntu-mc-5.cloudapp.net");
		locations.put(6, "ubuntu-mc-6.cloudapp.net");
		locArr=new int[] {4,5,6}; 
		zoneLocations.put(2, locArr);
		noOfLocationsWrtZone.put(2, locArr.length);
		
		locations.put(7, "ubuntu-mc-7.cloudapp.net");
		locations.put(8, "ubuntu-mc-8.cloudapp.net");
		locations.put(9, "ubuntu-mc-9.cloudapp.net");
		locArr=new int[] {7,8,9}; 
		zoneLocations.put(3, locArr);
		noOfLocationsWrtZone.put(3, locArr.length);
		
		noOfLocations = locations.size();
		
		locationRequestCount.put(1, 0);
		locationRequestCount.put(2, 0);
		locationRequestCount.put(3, 0);
		locationRequestCount.put(4, 0);
		locationRequestCount.put(5, 0);
		locationRequestCount.put(6, 0);
		locationRequestCount.put(7, 0);
		locationRequestCount.put(8, 0);
		locationRequestCount.put(9, 0);
		
		
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
	
	
	public HashMap<Integer, Integer> getNoOfLocationsWrtZone() {
		return noOfLocationsWrtZone;
	}

	public HashMap<Integer, int[]> getZoneLocations() {
		return zoneLocations;
	}

	public static synchronized LocationAwareConstants getInstance() {
		if(locationAwareConstants == null) {
			locationAwareConstants = new LocationAwareConstants();
		}
		return locationAwareConstants;
	}

	public int getNoOfLocations() {
		return noOfLocations;
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
