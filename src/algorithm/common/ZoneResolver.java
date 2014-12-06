package algorithm.common;
import java.text.SimpleDateFormat;
import java.util.*;

import algorithm.ant.AntConstants;
import algorithm.pso.PSOConstants;


public class ZoneResolver
{

	int numOfZones;
	private static ZoneResolver zoneAwareAlgorithm;
	public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
	int zone;
	int request;
	
	
	public int getRequest() {
		return request;
	}


	public void setRequest(int request) {
		this.request = request;
	}
	
	private ZoneResolver() {
		numOfZones = ZoneResolverConstants.getInstance().getNoOfZones();
	}



	public static synchronized ZoneResolver getInstance() {
		if(zoneAwareAlgorithm == null) {
			zoneAwareAlgorithm = new ZoneResolver();
		}
		return zoneAwareAlgorithm;
	}


	
	
	public int runLocationAwareAlgorithm(Double[] inputLocation) {

		int tempDistance = 0;
		int tempLocation = 0;
		HashMap<Integer, Double[]> tempMap = new HashMap<Integer,Double[]>();

		for(int i=1;i<=numOfZones;i++)
		{
				tempMap.put(i, ZoneResolverConstants.getInstance().geoZone.get(i));
				System.out.println("In Zone Resolver: TempLocation<key>:"+i+" <Lattitude>:"+ZoneResolverConstants.getInstance().geoZone.get(i)[0]+" <Longitude>:"+ZoneResolverConstants.getInstance().geoZone.get(i)[1]);
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
		zone = tempLocation;
		return zone;
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


