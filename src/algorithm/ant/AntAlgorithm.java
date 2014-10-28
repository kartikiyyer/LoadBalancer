package algorithm.ant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


public class AntAlgorithm {
	
	/*
	 * Pheromone table
	 * TODO: This is a temporary structure which would later be stored in database.
	 */
	private HashMap<Integer, Double> pheromoneTable = new HashMap<Integer, Double>();
	private double cpu;
	private double hd;
	private double ram;
	private static AntAlgorithm antAlgorithm;
	
	private AntAlgorithm() {
		int i;
		// Inserting into pheromone table
		for(i=1;i<=AntConstants.getInstance().getNoOfLocations();i++) {
			pheromoneTable.put(i, AntConstants.getInstance().getPheromone());
		}
	}
	
	
	public static synchronized AntAlgorithm getInstance() {
		if(antAlgorithm == null) {
			antAlgorithm = new AntAlgorithm();
		}
		return antAlgorithm;
	}
	
	
	public double getCpu() {
		return cpu;
	}


	public void setCpu(double cpu) {
		this.cpu = cpu;
	}


	public double getHd() {
		return hd;
	}


	public void setHd(double hd) {
		this.hd = hd;
	}


	public double getRam() {
		return ram;
	}


	public void setRam(double ram) {
		this.ram = ram;
	}


	public void printPheromoneTable() {
		int i;
		// Printing pheromone table
		for(i=1;i<=AntConstants.getInstance().getNoOfLocations();i++) {
			System.out.println("Location:" + i + " " + pheromoneTable.get(i));
		}
	}
	
	
	public int getLocationWithHighestPheromoneCount() {
		Iterator<Entry<Integer, Double>> it = pheromoneTable.entrySet().iterator();
		double maxValue = 0, minValue = AntConstants.getInstance().getPheromone();
		int location = 0;
		while (it.hasNext()) {
			Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
			if(pair.getValue() > maxValue) {
				maxValue = pair.getValue();
				location = pair.getKey(); 
			}
			if(pair.getValue() < minValue) {
				minValue = pair.getValue();
			}
		}
		
		// If all locations have the same value. Then randomly select one location.
		if(maxValue == minValue) {
			Random random = new Random();
			location = random.nextInt(pheromoneTable.size()) + 1;
		}
		return location;
	}
	
	
	public void increasePheromoneCountOfLocation(int location) {
		
		double p = (pheromoneTable.get(location) + AntConstants.getInstance().getDeltaPheromone()) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		pheromoneTable.put(location, p);
	}
	
	
	public void decreasePheromoneCountOfLocation(int location) {
		
		double p = pheromoneTable.get(location) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
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
		if(!(AntConstants.getInstance().isCPUAvailable(location, cpu) && AntConstants.getInstance().isHDAvailable(location, hd) && AntConstants.getInstance().isRAMAvailable(location, ram))) {
			// If not decrease the pheromone count of that location and start the search again.
			decreasePheromoneCountOfLocation(location);
			location = antBasedControl();
		}
		
		// TODO: Check how this algorithm works. If not able to distribute uniformly, uncomment below code.
		decreasePheromoneCountOfLocation(location);
		
		return location;
	}
} 
