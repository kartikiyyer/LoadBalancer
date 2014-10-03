package algorithm.ant;

import java.util.HashMap;

public class AntConstants {
	public static int NO_OF_LOCATIONS;
	public static double PHEROMONE = 0.5;
	public static double DELTA_PHEROMONE;
	public static HashMap<Integer, String> locations = new HashMap<Integer, String>();
	public static HashMap<Integer, Double> locationCPU = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> locationHD = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> locationRAM = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
	public static HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
	
	static {
		locations.put(1, "ubuntu-mc-1.cloudapp.net");
		locations.put(2, "ubuntu-mc-2.cloudapp.net");
		locations.put(3, "ubuntu-mc-3.cloudapp.net");
		locations.put(4, "ubuntu-mc-4.cloudapp.net");
		locations.put(5, "ubuntu-mc-5.cloudapp.net");		
		
		NO_OF_LOCATIONS = locations.size();
		DELTA_PHEROMONE = 1 / NO_OF_LOCATIONS;
		
	}
	
	public static void initializeLocationDetails(HashMap<Integer, Double> locationCPU, HashMap<Integer, Double> locationHD, HashMap<Integer, Double> locationRAM,
			HashMap<Integer, Double> locationMaxCPU, HashMap<Integer, Double> locationMaxHD, HashMap<Integer, Double> locationMaxRAM) {
		AntConstants.locationCPU = locationCPU;
		AntConstants.locationHD = locationHD;
		AntConstants.locationRAM = locationRAM;
		AntConstants.locationMaxCPU = locationMaxCPU;
		AntConstants.locationMaxHD = locationMaxHD;
		AntConstants.locationMaxRAM = locationMaxRAM;
		
	}
	
	public static void updateLocationDetails(int location, double locationCPU, double locationHD, double locationRAM) {
		AntConstants.locationCPU.put(location, AntConstants.locationCPU.get(location) + locationCPU);
		AntConstants.locationHD.put(location, AntConstants.locationHD.get(location) + locationHD);
		AntConstants.locationRAM.put(location, AntConstants.locationRAM.get(location) + locationRAM);
	}
	
	public static boolean isCPUAvailable(int location, double locationCPU) {
		if(AntConstants.locationMaxCPU.get(location) >= (AntConstants.locationCPU.get(location) + locationCPU)) {
			return true;
		}
		return false;
	}
	
	public static boolean isHDAvailable(int location, double locationHD) {
		if(AntConstants.locationMaxHD.get(location) >= (AntConstants.locationHD.get(location) + locationHD)) {
			return true;
		}
		return false;
	}
	
	public static boolean isRAMAvailable(int location, double locationRAM) {
		if(AntConstants.locationMaxRAM.get(location) >= (AntConstants.locationRAM.get(location) + locationRAM)) {
			return true;
		}
		return false;
	}
	
}
