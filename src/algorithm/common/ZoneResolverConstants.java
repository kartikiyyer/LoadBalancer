package algorithm.common;

import java.util.HashMap;

import algorithm.pso.PSOAlgorithm;
import algorithm.pso.PSOConstants;

public class ZoneResolverConstants {
	HashMap<Integer, Double[]> geoLocation = new HashMap<Integer,Double[]>();
	Double laLn1[] = new Double[]{32.7157380,-117.1610840};
	Double laLn2[] = new Double[]{40.712784,-74.005941};
	Double laLn3[] = new Double[]{43.712784,-54.005941};
	//Double laLn4[] = new Double[]{52.712784,-84.005941};
	
	private int noOfLocations;
	private HashMap<Integer, Integer> locations = new HashMap<Integer, Integer>();
	//Double laLn2[] = new Double[]{37.3393860,-121.8949550};
	private static ZoneResolverConstants locationAwareConstants;
	private ZoneResolverConstants()
	{
		locations.put(1, 1);
		locations.put(2, 2);
		locations.put(3, 3);
		//locations.put(4, "ubuntu-mc-4.cloudapp.net");
		
		geoLocation.put(1,laLn1);
		geoLocation.put(2,laLn2);
		geoLocation.put(3,laLn3);
		//geoLocation.put(4,laLn4);
		
		noOfLocations = locations.size();
	}
	

	public static synchronized ZoneResolverConstants getInstance() {
		if(locationAwareConstants == null) {
			locationAwareConstants = new ZoneResolverConstants();
		}
		return locationAwareConstants;
	}

	public HashMap<Integer, Integer> getLocations() {
		return locations;
	}
	
	public int getNoOfLocations() {
		return noOfLocations;
	}
}
