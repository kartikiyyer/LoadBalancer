package algorithm.location;
import java.util.*;

import algorithm.pso.PSOAlgorithm;

public class LocationAwareAlgorithm
{

	private static LocationAwareAlgorithm locationAwareAlgorithm;
	public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
	int location;
	private LocationAwareAlgorithm() {

	}



	public static synchronized LocationAwareAlgorithm getInstance() {
		if(locationAwareAlgorithm == null) {
			locationAwareAlgorithm = new LocationAwareAlgorithm();
		}
		return locationAwareAlgorithm;
	}



	public int runLocationAwareAlgorithm(Double[] inputLocation) {

		int tempDistance = 0;
		int tempLocation = 0;
		Iterator it = LocationAwareConstants.getInstance().geoLocation.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
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
		location = tempLocation;

		return location;
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




	/*public static void main (String[] args) throws java.lang.Exception
	{

		LocationAwareAlgorithm one = new LocationAwareAlgorithm();
		Double laLn[] = new Double[]{32.7157380,-117.1610840};
		Double laLn1[] = new Double[]{37.7749290,-122.4194160};
		Double laLn2[] = new Double[]{37.3393860,-121.8949550};
		one.insertIntoHashMap(1,laLn);
		one.insertIntoHashMap(2,laLn1);
		one.insertIntoHashMap(3,laLn2);
		one.printMap();
	}*/
}


