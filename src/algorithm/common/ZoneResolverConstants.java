package algorithm.common;

import java.util.HashMap;

import algorithm.pso.PSOAlgorithm;
import algorithm.pso.PSOConstants;

public class ZoneResolverConstants {
	HashMap<Integer, Double[]> geoZone = new HashMap<Integer,Double[]>();
	Double laLn1[] = new Double[]{32.7157380,-117.1610840};
	Double laLn2[] = new Double[]{40.712784,-74.005941};
	Double laLn3[] = new Double[]{43.712784,-54.005941};
	//Double laLn4[] = new Double[]{52.712784,-84.005941};
	
	private int noOfZones;
	private HashMap<Integer, Integer> zones = new HashMap<Integer, Integer>();
	//Double laLn2[] = new Double[]{37.3393860,-121.8949550};
	private static ZoneResolverConstants zoneAwareConstants;
	private ZoneResolverConstants()
	{
		zones.put(1, 1);
		zones.put(2, 2);
		zones.put(3, 3);
		//locations.put(4, "ubuntu-mc-4.cloudapp.net");
		
		geoZone.put(1,laLn1);
		geoZone.put(2,laLn2);
		geoZone.put(3,laLn3);
		//geoLocation.put(4,laLn4);
		
		noOfZones = zones.size();
	}
	

	public static synchronized ZoneResolverConstants getInstance() {
		if(zoneAwareConstants == null) {
			zoneAwareConstants = new ZoneResolverConstants();
		}
		return zoneAwareConstants;
	}

	public HashMap<Integer, Integer> getZones() {
		return zones;
	}
	
	public int getNoOfZones() {
		return noOfZones;
	}
}
