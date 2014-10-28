package algorithm.location;

import java.util.HashMap;

import algorithm.pso.PSOConstants;

public class LocationAwareConstants {
	HashMap<Integer, Double[]> geoLocation = new HashMap<Integer,Double[]>();
	Double laLn[] = new Double[]{32.7157380,-117.1610840};
	Double laLn1[] = new Double[]{40.712784,-74.005941};
	//Double laLn2[] = new Double[]{37.3393860,-121.8949550};
	private static LocationAwareConstants locationAwareConstants;
	private LocationAwareConstants()
	{
		geoLocation.put(1,laLn);
		geoLocation.put(2,laLn1);
		//geoLocation.put(3,laLn2);
	}

	public static synchronized LocationAwareConstants getInstance() {
		if(locationAwareConstants == null) {
			locationAwareConstants = new LocationAwareConstants();
		}
		return locationAwareConstants;
	}

	public HashMap<Integer, Double[]> getLocations() {
		return geoLocation;
	}
}
