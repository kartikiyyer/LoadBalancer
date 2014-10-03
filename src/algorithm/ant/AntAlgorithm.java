package algorithm.ant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class AntAlgorithm {
	
	/*
	 * Pheromone table
	 * TODO: This is a temporary structure which would later be stored in database.
	 */
	private HashMap<Integer, Double> pheromoneTable = new HashMap<Integer, Double>();
	private double cpu;
	private double hd;
	private double ram;
	
	public AntAlgorithm(double cpu, double hd, double ram) {
		this.cpu = cpu;
		this.hd = hd;
		this.ram = ram;
	}
	
	public void initializePheromoneTable(double pheromone) {
		int i;
		// Inserting into pheromone table
		for(i=1;i<=AntConstants.NO_OF_LOCATIONS;i++) {
			pheromoneTable.put(i, pheromone);
		}
	}
	
	public void printPheromoneTable() {
		int i;
		// Printing pheromone table
		for(i=1;i<=AntConstants.NO_OF_LOCATIONS;i++) {
			System.out.println(pheromoneTable.get(i));
		}
	}
	
	
	public int getLocationWithHighestPheromoneCount() {
		Iterator<Entry<Integer, Double>> it = pheromoneTable.entrySet().iterator();
		int maxValue = 0;
		int location = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
			if(pair.getValue() > maxValue) {
				location = pair.getKey(); 
			}
		}
		return location;
	}
	
	
	public void increasePheromoneCountOfLocation(int location) {
		
		double p = (pheromoneTable.get(location) + AntConstants.DELTA_PHEROMONE) / (1 + AntConstants.DELTA_PHEROMONE);
		
		pheromoneTable.put(location, p);
	}
	
	
	public void decreasePheromoneCountOfLocation(int location) {
		
		double p = pheromoneTable.get(location) / (1 + AntConstants.DELTA_PHEROMONE);
		
		pheromoneTable.put(location, p);
	}
	
	
	/**
	 * The is the actual logic / algorithm.
	 * @return
	 */
	
	public int antBasedControl() {
		
		/*
		 * Algorithm steps:-
		 * 1. The centralized server will look at the pheromone table and check if there is any preference.
		 * 2. For the first time all will have the same probability. So server will send the request to any of the location.
		 * 3. The location which will send its response first after completing its job, 
		 *    will update the routing table with more preference and is the shortest path.
		 * 4. We will need to store the status of the request served as more the server request served has high pheromone as per the algorithm.
		 * 5. At the start server will send requests to all locations. The location having less response time will become suitable for 
		 * 	  next request.
		 */
		
		
		// Fetch the location with highest pheromone count.
		// This would be our candidate for processing request.
		int location = getLocationWithHighestPheromoneCount();
		System.out.println("Location having highest pheromone count: " + location);
		
		
		// Check whether there are sufficient amount of resource available on that location.
		if(!(AntConstants.isCPUAvailable(location, CPU) && AntConstants.isHDAvailable(location, HD) && AntConstants.isRAMAvailable(location, RAM))) {
			// If not decrease the pheromone count of that location and start the search again.
			location = antBasedControl();
		}
				
		return location;
	}
	
	
	public static void main(String[] args) {
		
	}
} 
