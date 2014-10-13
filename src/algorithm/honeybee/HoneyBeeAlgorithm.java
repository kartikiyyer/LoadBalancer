package algorithm.honeybee;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class HoneyBeeAlgorithm {

	
	/*
	 * Fitness table
	 * TODO: This is a temporary structure which would later be stored in database.
	 */
	private HashMap<Integer, Double> fitnessTable = new HashMap<Integer, Double>();
	private HashMap<Integer, List<String>> reqResTimeLogTable = new HashMap<Integer, List<String>>();
	
	private double cpu;
	private double hd;
	private double ram;
	private static HoneyBeeAlgorithm honeybeeAlgorithm;
	
	private HoneyBeeAlgorithm() {
		int i;
		// Inserting into fitness table
		for(i=1;i<=HoneyBeeConstants.getInstance().getNoOfLocations();i++) {
			fitnessTable.put(i, HoneyBeeConstants.getInstance().getFitness());
		}
	}
	
	public static synchronized HoneyBeeAlgorithm getInstance() {
		if(honeybeeAlgorithm == null) {
			honeybeeAlgorithm = new HoneyBeeAlgorithm();
		}
		return honeybeeAlgorithm;
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

	public int processHoneyBeeAlgorithm(double cpu, double storage, double ram, double time){

		HoneyBeeAlgorithm hbAlgorithm = HoneyBeeAlgorithm.getInstance();
		hbAlgorithm.setCpu(cpu);
		hbAlgorithm.setHd(storage);
		hbAlgorithm.setRam(ram);
		//aa.printPheromoneTable();
		
		System.out.println("After request");	
		
		int location = hbAlgorithm.honeybeeBasedControl();
//		System.out.println(AntConstants.getInstance().getDeltaPheromone());
		// Increase amount of resources allocated.
		HoneyBeeConstants.getInstance().increaseLocationDetails(location, cpu, storage, ram);
		
		System.out.println(location);	
		System.out.println(HoneyBeeConstants.getInstance().getLocationCPU());
		System.out.println(HoneyBeeConstants.getInstance().getLocationHD());
		System.out.println(HoneyBeeConstants.getInstance().getLocationRAM());
		
		System.out.println(HoneyBeeConstants.getInstance().getLocationMaxCPU());
		System.out.println(HoneyBeeConstants.getInstance().getLocationMaxHD());
		System.out.println(HoneyBeeConstants.getInstance().getLocationMaxRAM());
		System.out.println();
//		hbAlgorithm.printFitnessTable();
	
		return location;
	}

	public void printFitnessTable() {
		int i;
		// Printing fitness table
		for(i=1;i<=HoneyBeeConstants.getInstance().getNoOfLocations();i++) {
			System.out.println(fitnessTable.get(i));
		}
	}
	
	
	public int getLocationWithHighestFitnessValue() {
		Iterator<Entry<Integer, Double>> it = fitnessTable.entrySet().iterator();
		double maxValue = 0, minValue = HoneyBeeConstants.getInstance().getFitness();
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
			location = random.nextInt(fitnessTable.size()) + 1;
		}
		return location;
	}
	
	
	/*public void increaseFitnessValueOfLocation(int location) {
		
		double p = (fitnessTable.get(location) + AntConstants.getInstance().getDeltaPheromone()) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		fitnessTable.put(location, p);
	}
	
	
	public void decreaseFitnessValueOfLocation(int location) {
		
		double p = fitnessTable.get(location) / (1 + AntConstants.getInstance().getDeltaPheromone());
		
		fitnessTable.put(location, p);
	}*/
	
	
	public HashMap<Integer, List<String>> getReqResTimeLogTable() {
		return reqResTimeLogTable;
	}

	public void setReqResTimeLogTable(
			HashMap<Integer, List<String>> reqResTimeLogTable) {
		this.reqResTimeLogTable = reqResTimeLogTable;
	}

	/**
	 * The is the actual logic / algorithm.
	 * @return
	 */
	
	public int honeybeeBasedControl() {
		
		// Fetch the location with highest fitness value.
		// This would be our candidate for processing request.
		int location = getLocationWithHighestFitnessValue();
		System.out.println("Location having highest Fitness: " + location);
		
		
		// Check whether there are sufficient amount of resource available on that location.
		if(!(HoneyBeeConstants.getInstance().isCPUAvailable(location, cpu) && HoneyBeeConstants.getInstance().isHDAvailable(location, hd) && HoneyBeeConstants.getInstance().isRAMAvailable(location, ram))) {
			//keep the request waiting untill any server is free to serve this request
			//TODO: enqueue the request
			System.out.println("waiting for free server........");
		}
		
//		decreaseFitnessValueOfLocation(location);
		return location;
	}
	
}
