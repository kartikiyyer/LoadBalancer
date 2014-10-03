package algorithm.ant;

import java.util.HashMap;

public class AntConstants {
	public static int NO_OF_LOCATIONS;
	public static double PHEROMONE = 0.5;
	public static double DELTA_PHEROMONE;
	public static HashMap<Integer, String> locations = new HashMap<Integer, String>();
	
	
	static {
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		locations.put(3, "ubuntu-mc-3.cloudapp.net");
		locations.put(4, "ubuntu-mc-4.cloudapp.net");
		locations.put(5, "ubuntu-mc-5.cloudapp.net");
		NO_OF_LOCATIONS = locations.size();
		DELTA_PHEROMONE = 1 / NO_OF_LOCATIONS;
	}
	
}
