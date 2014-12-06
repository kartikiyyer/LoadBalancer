package load;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuTimer;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.management.OperatingSystemMXBean;

import algorithm.ant.AntAlgorithm;
import algorithm.ant.AntConstants;
import algorithm.common.ZoneResolver;
import algorithm.honeybee.HoneyBeeAlgorithm;
import algorithm.honeybee.HoneyBeeConstants;
import algorithm.location.LocationAwareAlgorithm;
import algorithm.location.LocationAwareConstants;
import algorithm.pso.PSOAlgorithm;
import algorithm.pso.PSOConstants;

@Path("/")
public class RequestController {	

	@Path("/request")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainText() {	

		HashMap<Integer, String> locations = AntConstants.getInstance().getLocations();

		HashMap<Integer, Double> locationCPU = new HashMap<Integer, Double>();
		HashMap<Integer, Double> locationHD = new HashMap<Integer, Double>();
		HashMap<Integer, Double> locationRAM = new HashMap<Integer, Double>();
		HashMap<Integer, Double> locationMaxCPU = new HashMap<Integer, Double>();
		HashMap<Integer, Double> locationMaxHD = new HashMap<Integer, Double>();
		HashMap<Integer, Double> locationMaxRAM = new HashMap<Integer, Double>();
		
		Iterator<Map.Entry<Integer, String>> itr = locations.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<Integer, String> pairs = itr.next();
			System.out.println(pairs.getValue());

			String url = "http://"+pairs.getValue()+":8080/Instance/system-information";
			String charset = "UTF-8";
			int status = 0;

			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setRequestProperty("Accept-Charset", charset);

				status = connection.getResponseCode();

				if(status == 200) {
					InputStream stream = connection.getInputStream();
					InputStreamReader isReader = new InputStreamReader(stream ); 

					//put output stream into a string
					BufferedReader br = new BufferedReader(isReader);
					String systemInformation = br.readLine();
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(systemInformation);
					locationCPU.put(pairs.getKey(),Double.parseDouble((String) obj.get("cpu")));
					locationHD.put(pairs.getKey(),Double.parseDouble((String) obj.get("hd")));
					locationRAM.put(pairs.getKey(),Double.parseDouble((String) obj.get("ram")));
					locationMaxCPU.put(pairs.getKey(),Double.parseDouble((String) obj.get("cpuMax")));
					locationMaxHD.put(pairs.getKey(),Double.parseDouble((String) obj.get("hdMax")));
					locationMaxRAM.put(pairs.getKey(),Double.parseDouble((String) obj.get("ramMax")));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Initialize with the system information.
		AntConstants.getInstance().initializeLocationDetails(locationCPU, locationHD, locationRAM, locationMaxCPU, locationMaxHD, locationMaxRAM);
		// Initialize with the system information.
		HoneyBeeConstants.getInstance().initializeLocationDetails(locationCPU, locationHD, locationRAM, locationMaxCPU, locationMaxHD, locationMaxRAM);
		// Initialize with the system information.
		PSOConstants.getInstance().initializeLocationDetails(locationCPU, locationHD, locationRAM, locationMaxCPU, locationMaxHD, locationMaxRAM);
		// Initialize with the system information.
		LocationAwareConstants.getInstance().initializeLocationDetails(locationCPU, locationHD, locationRAM, locationMaxCPU, locationMaxHD, locationMaxRAM);


		System.out.println("Printing location details.");
		System.out.println(AntConstants.getInstance().getLocationCPU());
		System.out.println(AntConstants.getInstance().getLocationHD());
		System.out.println(AntConstants.getInstance().getLocationRAM());

		System.out.println(AntConstants.getInstance().getLocationMaxCPU());
		System.out.println(AntConstants.getInstance().getLocationMaxHD());
		System.out.println(AntConstants.getInstance().getLocationMaxRAM());

		return "hello";
	}

	@Path("/request")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String processRequestParameter(@FormParam("cpu") double cpu, @FormParam("storage") double storage, @FormParam("ram") double ram, @FormParam("time") double time, @FormParam ("algoIdentifier") int algoIdentifier,@FormParam("requestType") int requestType, @FormParam("latitude") double latitude, @FormParam("longitude") double longitude) {
		int request = 0;

		System.out.println("Start:"+ System.currentTimeMillis());
		ZoneResolver zoneAwareAlgorithm= ZoneResolver.getInstance();
		Double inputLocation1[] = new Double[]{latitude,longitude};
		int zone=zoneAwareAlgorithm.runLocationAwareAlgorithm(inputLocation1);
		//		System.out.println("location of server "  + zone);
		System.out.println("Zone Selected for processing Request: "+zone);
		//		return "Zone Selected for processing Request: "+zone;
		//TODO remove this temp variable in production..just for debugging
		int temp=-999;
		if(algoIdentifier==1){
			try{
				System.out.println("Start:"+ System.currentTimeMillis());
				HoneyBeeAlgorithm hbAlgorithm=HoneyBeeAlgorithm.getInstance();
				//changes
				int location=hbAlgorithm.processHoneyBeeAlgorithm(cpu,storage,ram,time,requestType,zone);
				System.out.println("cpu "+HoneyBeeConstants.getInstance().getLocationCPU());
				System.out.println("@@@@@@@@@@@@@@");

				HoneyBeeAlgorithm.getInstance().setRequest(HoneyBeeAlgorithm.getInstance().getRequest()+1);
				request=HoneyBeeAlgorithm.getInstance().getRequest();
				//				request++;
				System.out.println("request number: "+request);
				System.out.println("Sending on location: "+HoneyBeeConstants.getInstance().getLocations().get(location));
				
				//calculate cost for this request
				Double[] locCost = new Double[3];
				locCost=HoneyBeeConstants.locationCost.get(location);
				HoneyBeeConstants.costHM.put(request, ((locCost[0]*cpu)+(locCost[1]*storage)+(locCost[2]*ram)));
				HoneyBeeAlgorithm.getInstance().setReqCost(HoneyBeeConstants.costHM);
				//cost ends
				
				//				int status = forwardRequest(HoneyBeeConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);
				int status=200; //TODO temporary..since no actual servers...later on uncomment above line and comment this line
								
				if(status == 200) {
					HoneyBeeAlgorithm.getInstance().processTimeLogForRequest(request,location,requestType);
					HoneyBeeConstants.getInstance().increaseLocationRequestCount(location);
					temp=location;
				}
				System.out.println("End:"+System.currentTimeMillis());
			}catch(Exception e){
				e.printStackTrace();
			}

		} else if(algoIdentifier==2){
			try{
				System.out.println("Start:"+ System.currentTimeMillis());
				AntAlgorithm aa = AntAlgorithm.getInstance();
				aa.setCpu(cpu);
				aa.setHd(storage);
				aa.setRam(ram);
				//aa.printPheromoneTable();

				System.out.println("After request");	

				int location = aa.antBasedControl();
				//System.out.println(AntConstants.getInstance().getDeltaPheromone());
				// Increase amount of resources allocated.
				AntConstants.getInstance().increaseLocationDetails(location, cpu, storage, ram);
				// Decrease amount of allocated resources.
				//AntConstants.decreaseLocationMaxDetails(location, cpu, storage, ram);

				AntAlgorithm.getInstance().setRequest(AntAlgorithm.getInstance().getRequest() + 1);
				request = AntAlgorithm.getInstance().getRequest();
				System.out.println("Request: "  + request + " would be sent to location: " + AntConstants.getInstance().getLocations().get(location));	

				//calculate cost for this request
				Double[] locCost = new Double[3];
				locCost=AntConstants.locationCost.get(location);
				AntConstants.costHM.put(request, ((locCost[0]*cpu)+(locCost[1]*storage)+(locCost[2]*ram)));
				AntAlgorithm.getInstance().setReqCost(AntConstants.costHM);
				//cost ends				
				
				System.out.println("Printing pheromone table.");
				aa.printPheromoneTable();

				int status = forwardRequest(AntConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);

				if(status == 200) {
					AntAlgorithm.getInstance().processTimeLogForRequest(request,location,requestType);
					AntConstants.getInstance().increaseLocationRequestCount(location);
				}
				System.out.println("End:"+ System.currentTimeMillis());
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		 

		else if(algoIdentifier == 3){
			try{
				System.out.println("Start:"+ System.currentTimeMillis());
				LocationAwareAlgorithm locationAwareAlgorithm= LocationAwareAlgorithm.getInstance();
				Double inputLocation[] = new Double[]{latitude,longitude};
				int location=locationAwareAlgorithm.runLocationAwareAlgorithm(inputLocation,zone, cpu,storage,ram);
				System.out.println("location of server "  + location);
				temp=location;
				LocationAwareAlgorithm.getInstance().setRequest(LocationAwareAlgorithm.getInstance().getRequest() + 1);
				request = LocationAwareAlgorithm.getInstance().getRequest();
				
				//calculate cost for this request
				Double[] locCost = new Double[3];
				locCost=LocationAwareConstants.locationCost.get(location);
				LocationAwareConstants.costHM.put(request, ((locCost[0]*cpu)+(locCost[1]*storage)+(locCost[2]*ram)));
				LocationAwareAlgorithm.getInstance().setReqCost(LocationAwareConstants.costHM);
				//cost ends
				
				//				TODO uncomment below line
				//				int status = forwardRequest(HoneyBeeConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);
				//				TODO for development purpose onle, comment below line in production
				int status=200;
				if(status == 200) {
					LocationAwareAlgorithm.getInstance().processTimeLogForRequest(request,location,requestType);
				}
				System.out.println("End:"+ System.currentTimeMillis());
			}catch(Exception e){
				e.printStackTrace();
			}

		}
			//PSO Algorithm
				else if(algoIdentifier == 4){
					try{
						System.out.println("Start:"+ System.currentTimeMillis());
						PSOAlgorithm psoAlgorithm = PSOAlgorithm.getInstance();
						int location = psoAlgorithm.runPSOAlgorithm(cpu,storage,ram,time,request);
						PSOAlgorithm.getInstance().setRequest(PSOAlgorithm.getInstance().getRequest() + 1);
						request = PSOAlgorithm.getInstance().getRequest();
						int status = forwardRequest(PSOConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);
						
						//calculate cost for this request
						Double[] locCost = new Double[3];
						locCost=PSOConstants.locationCost.get(location);
						PSOConstants.costHM.put(request, ((locCost[0]*cpu)+(locCost[1]*storage)+(locCost[2]*ram)));
						PSOAlgorithm.getInstance().setReqCost(PSOConstants.costHM);
						//cost ends
						
						//int status = 200;
						if(status == 200) {
							PSOAlgorithm.getInstance().processTimeLogForRequest(request,location,requestType);
						}
						System.out.println("End:"+ System.currentTimeMillis());
					}catch(Exception e){
						e.printStackTrace();
					}

				}


		return "";
	}

	public static int forwardRequest(String location, String locationId, String request, String cpu, String storage, String ram, String time, int algoIdentifier, int requestType) {
		String url = "http://"+location+":8080/Instance/request";
		String charset = "UTF-8";
		int status = 0;
		String serverName="";
		try {
			if(algoIdentifier==2){
				serverName=AntConstants.getInstance().getServer();
			}else if(algoIdentifier==1){
				serverName=HoneyBeeConstants.getInstance().getServer();
			}else if(algoIdentifier==3){
				serverName=HoneyBeeConstants.getInstance().getServer();
			}else if(algoIdentifier==4){
				serverName=HoneyBeeConstants.getInstance().getServer();
			}


			String query = String.format("server=%s&location=%s&request=%s&cpu=%s&storage=%s&ram=%s&time=%s&algoIdentifier=%s&requestType=%s", 
					URLEncoder.encode(serverName, charset),
					URLEncoder.encode(locationId, charset),
					URLEncoder.encode(request, charset),
					URLEncoder.encode(cpu, charset), 
					URLEncoder.encode(storage, charset),
					URLEncoder.encode(ram, charset),
					URLEncoder.encode(time, charset),
					URLEncoder.encode(String.valueOf(algoIdentifier), charset),
					URLEncoder.encode(String.valueOf(requestType), charset));

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			//connection.connect();
			try (OutputStream output = connection.getOutputStream()) {
				output.write(query.getBytes(charset));
				output.flush();
				output.close();
			}


			status = connection.getResponseCode();

			//System.out.println("This is the status from server: "+ status);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return status;
	}


	@Path("/response")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String processResponseParameter(@FormParam("location") int location, @FormParam("request") int request, @FormParam("cpu") double cpu, @FormParam("storage") double storage, @FormParam("ram") double ram, @FormParam("algoIdentifier") int algoIdentifier, @FormParam("requestType") int requestType) {

		if(algoIdentifier==1){

			try{
				String responseTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
				System.out.println(""+request+"  "+responseTimeStamp+"  "+requestType);
				HoneyBeeAlgorithm.getInstance().processTimeLogForResponse(request, responseTimeStamp, requestType);

				// increase available resources.
				System.out.println("location: "+location+"   CPU: "+cpu+"  STORAGE: "+storage+"   RAM: "+ram);
				HoneyBeeConstants.getInstance().decreaseLocationDetails(location, cpu, storage, ram);
				HoneyBeeAlgorithm.getInstance().calculateResponseTime(request,location,requestType);

				//				HoneyBeeAlgorithm.getInstance().processFitnessValue(location);
				//				hbalgorithm.increaseFitnessValueOfLocation(location);

				System.out.println("After all Time calculations in response:-->");	

				System.out.println("The Request number " + request +" was processed at "+location +" and response received now!");
				System.out.println(HoneyBeeConstants.getInstance().getLocationCPU());
				System.out.println(HoneyBeeConstants.getInstance().getLocationHD());
				System.out.println(HoneyBeeConstants.getInstance().getLocationRAM());

				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxCPU());
				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxHD());
				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxRAM());
				System.out.println("##Complete TimeLog (locationResponseTimeLogTable)##: "+HoneyBeeAlgorithm.getInstance().locationResponseTimeLogTable);
				System.out.println();
				//				HoneyBeeAlgorithm.getInstance().printFitnessTable();

				HoneyBeeConstants.getInstance().decreaseLocationRequestCount(location);
			}catch(Exception e){
				e.printStackTrace();
			}

		} else if(algoIdentifier==2){
			try{
				AntAlgorithm aa = AntAlgorithm.getInstance();

				// Decrease amount of resources allocated.
				AntConstants.getInstance().decreaseLocationDetails(location, cpu, storage, ram);
				aa.increasePheromoneCountOfLocation(location);

				System.out.println("After response");	

				System.out.println("Request: " + request + " served by location: " + AntConstants.getInstance().getLocations().get(location));

				AntConstants.getInstance().decreaseLocationRequestCount(location);

				String responseTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
				System.out.println(""+request+"  "+responseTimeStamp+"  "+requestType);
				AntAlgorithm.getInstance().processTimeLogForResponse(request, responseTimeStamp, requestType);

				AntAlgorithm.getInstance().calculateResponseTime(request,location,requestType);
			} catch(Exception e){
				e.printStackTrace();
			}

		} else if(algoIdentifier==3){

			try{
				LocationAwareAlgorithm locationAwareAlgorithm = LocationAwareAlgorithm.getInstance();

				// Decrease amount of resources allocated.
				locationAwareAlgorithm.decreaseLocationDetails(location, cpu, storage, ram);

				System.out.println("After response");	

				System.out.println(location);	
				System.out.println("Request: " + request);


				String responseTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
				System.out.println(""+request+"  "+responseTimeStamp+"  "+requestType);
				LocationAwareAlgorithm.getInstance().processTimeLogForResponse(request, responseTimeStamp, requestType);

				LocationAwareAlgorithm.getInstance().calculateResponseTime(request,location,requestType);

			}catch(Exception e){
				e.printStackTrace();
			}

		} else if(algoIdentifier==4){

			try{
				PSOAlgorithm psoAlgorithm = PSOAlgorithm.getInstance();

				// Decrease amount of resources allocated.
				psoAlgorithm.decreaseLocationDetails(location, cpu, storage, ram);

				System.out.println("After response");	

				System.out.println(location);	
				System.out.println("Request: " + request);

				String responseTimeStamp=new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
				System.out.println(""+request+"  "+responseTimeStamp+"  "+requestType);
				PSOAlgorithm.getInstance().processTimeLogForResponse(request, responseTimeStamp, requestType);

				PSOAlgorithm.getInstance().calculateResponseTime(request,location,requestType);

			}catch(Exception e){
				e.printStackTrace();
			}

		} 


		// TODO: Send response to client. Need to figure out how will this be processed by client.

		return "";
	}


	@Path("/request/response-time")
	@GET
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public String processResponseTime(@QueryParam("algoIdentifier") int algoIndentifier) {
		String result = "";
		if(algoIndentifier == 1) {

			HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = HoneyBeeAlgorithm.getInstance().locationResponseTimeLogTable;

			System.out.println("********************************");
			System.out.println("Honey Bee Algorithm");
			System.out.println("Task 1:::::");

			// location  requesttype   responsetime

			Iterator<Entry<Integer, HashMap<Integer,List>>> it = locationResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair = (Map.Entry<Integer, HashMap<Integer,List>>)it.next();

				System.out.println("Server Name: "+HoneyBeeConstants.getInstance().getLocations().get(pair.getKey()));

				Iterator<Entry<Integer,List>> it1 = (Iterator<Entry<Integer, List>>) pair.getValue().entrySet().iterator();

				while (it1.hasNext()) {
					Map.Entry<Integer,List> pair1 = (Map.Entry<Integer,List>)it1.next();
					if(pair1.getKey()!=0){
						System.out.println("Request Type : "+pair1.getKey());
						System.out.println("Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair1.getValue();
						for (Double double1 : al) {
							System.out.print(" "+double1 + ", ");
						}
						System.out.println();
					}
				}

			}

			System.out.println("Task 2:::::");
			HashMap<Integer, HashMap<Integer, List>> locationAverageResponseTimeLogTable = HoneyBeeAlgorithm.getInstance().locationAverageResponseTimeLogTable;

			Iterator<Entry<Integer, HashMap<Integer,List>>> it2 = locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair2 = (Map.Entry<Integer, HashMap<Integer,List>>)it2.next();

				System.out.println("Server Name: "+HoneyBeeConstants.getInstance().getLocations().get(pair2.getKey()));

				Iterator<Entry<Integer,List>> it3 = (Iterator<Entry<Integer, List>>) pair2.getValue().entrySet().iterator();

				while (it3.hasNext()) {
					Map.Entry<Integer,List> pair3 = (Map.Entry<Integer,List>)it3.next();
					if(pair3.getKey()!=0){
						System.out.println("Request Type : "+pair3.getKey());
						System.out.println("Average Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair3.getValue();
						System.out.print(" "+al.get(0));
						System.out.println();
					}
				}

			}


			System.out.println("********************************");





		} else if(algoIndentifier == 2) {


			HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = AntAlgorithm.getInstance().locationResponseTimeLogTable;

			System.out.println("********************************");
			System.out.println("Ant Algorithm");
			System.out.println("Task 1:::::");

			// location  requesttype   responsetime

			Iterator<Entry<Integer, HashMap<Integer,List>>> it = locationResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair = (Map.Entry<Integer, HashMap<Integer,List>>)it.next();

				System.out.println("Server Name: "+AntConstants.getInstance().getLocations().get(pair.getKey()));

				Iterator<Entry<Integer,List>> it1 = (Iterator<Entry<Integer, List>>) pair.getValue().entrySet().iterator();

				while (it1.hasNext()) {
					Map.Entry<Integer,List> pair1 = (Map.Entry<Integer,List>)it1.next();
					if(pair1.getKey()!=0){
						System.out.println("Request Type : "+pair1.getKey());
						System.out.println("Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair1.getValue();
						for (Double double1 : al) {
							System.out.print(" "+double1+", ");
						}
						System.out.println();
					}
				}

			}

			System.out.println("Task 2:::::");
			HashMap<Integer, HashMap<Integer, List>> locationAverageResponseTimeLogTable = AntAlgorithm.getInstance().locationAverageResponseTimeLogTable;

			Iterator<Entry<Integer, HashMap<Integer,List>>> it2 = locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair2 = (Map.Entry<Integer, HashMap<Integer,List>>)it2.next();

				System.out.println("Server Name: "+AntConstants.getInstance().getLocations().get(pair2.getKey()));

				Iterator<Entry<Integer,List>> it3 = (Iterator<Entry<Integer, List>>) pair2.getValue().entrySet().iterator();

				while (it3.hasNext()) {
					Map.Entry<Integer,List> pair3 = (Map.Entry<Integer,List>)it3.next();
					if(pair3.getKey()!=0){
						System.out.println("Request Type : "+pair3.getKey());
						System.out.println("Average Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair3.getValue();
						System.out.print(" "+al.get(0));
						System.out.println();
					}
				}

			}


			System.out.println("********************************");


		} else if(algoIndentifier == 3) {

			HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = LocationAwareAlgorithm.getInstance().locationResponseTimeLogTable;

			System.out.println("********************************");
			System.out.println("Location Aware Algorithm");
			System.out.println("Task 1:::::");

			// location  requesttype   responsetime

			Iterator<Entry<Integer, HashMap<Integer,List>>> it = locationResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair = (Map.Entry<Integer, HashMap<Integer,List>>)it.next();

				System.out.println("Server Name: "+LocationAwareConstants.getInstance().getLocations().get(pair.getKey()));

				Iterator<Entry<Integer,List>> it1 = (Iterator<Entry<Integer, List>>) pair.getValue().entrySet().iterator();

				while (it1.hasNext()) {
					Map.Entry<Integer,List> pair1 = (Map.Entry<Integer,List>)it1.next();
					if(pair1.getKey()!=0){
						System.out.println("Request Type : "+pair1.getKey());
						System.out.println("Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair1.getValue();
						for (Double double1 : al) {
							System.out.print(" "+double1+", ");
						}
						System.out.println();
					}
				}

			}

			System.out.println("Task 2:::::");
			HashMap<Integer, HashMap<Integer, List>> locationAverageResponseTimeLogTable = LocationAwareAlgorithm.getInstance().locationAverageResponseTimeLogTable;

			Iterator<Entry<Integer, HashMap<Integer,List>>> it2 = locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair2 = (Map.Entry<Integer, HashMap<Integer,List>>)it2.next();

				System.out.println("Server Name: "+LocationAwareConstants.getInstance().getLocations().get(pair2.getKey()));

				Iterator<Entry<Integer,List>> it3 = (Iterator<Entry<Integer, List>>) pair2.getValue().entrySet().iterator();

				while (it3.hasNext()) {
					Map.Entry<Integer,List> pair3 = (Map.Entry<Integer,List>)it3.next();
					if(pair3.getKey()!=0){
						System.out.println("Request Type : "+pair3.getKey());
						System.out.println("Average Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair3.getValue();
						System.out.print(" "+al.get(0));
						System.out.println();
					}
				}

			}


			System.out.println("********************************");

		} else if(algoIndentifier == 4) {

			HashMap<Integer, HashMap<Integer,List>> locationResponseTimeLogTable = PSOAlgorithm.getInstance().locationResponseTimeLogTable;

			System.out.println("********************************");
			System.out.println("PSO Algorithm");
			System.out.println("Task 1:::::");

			// location  requesttype   responsetime

			Iterator<Entry<Integer, HashMap<Integer,List>>> it = locationResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair = (Map.Entry<Integer, HashMap<Integer,List>>)it.next();

				System.out.println("Server Name: "+PSOConstants.getInstance().getLocations().get(pair.getKey()));

				Iterator<Entry<Integer,List>> it1 = (Iterator<Entry<Integer, List>>) pair.getValue().entrySet().iterator();

				while (it1.hasNext()) {
					Map.Entry<Integer,List> pair1 = (Map.Entry<Integer,List>)it1.next();
					if(pair1.getKey()!=0){
						System.out.println("Request Type : "+pair1.getKey());
						System.out.println("Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair1.getValue();
						for (Double double1 : al) {
							System.out.print(" "+double1+", ");
						}
						System.out.println();
					}
				}

			}

			System.out.println("Task 2:::::");
			HashMap<Integer, HashMap<Integer, List>> locationAverageResponseTimeLogTable = PSOAlgorithm.getInstance().locationAverageResponseTimeLogTable;

			Iterator<Entry<Integer, HashMap<Integer,List>>> it2 = locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry<Integer, HashMap<Integer,List>> pair2 = (Map.Entry<Integer, HashMap<Integer,List>>)it2.next();

				System.out.println("Server Name: "+PSOConstants.getInstance().getLocations().get(pair2.getKey()));

				Iterator<Entry<Integer,List>> it3 = (Iterator<Entry<Integer, List>>) pair2.getValue().entrySet().iterator();

				while (it3.hasNext()) {
					Map.Entry<Integer,List> pair3 = (Map.Entry<Integer,List>)it3.next();
					if(pair3.getKey()!=0){
						System.out.println("Request Type : "+pair3.getKey());
						System.out.println("Average Response Time--> ");
						ArrayList<Double> al = (ArrayList<Double>) pair3.getValue();
						System.out.print(" "+al.get(0));
						System.out.println();
					}
				}

			}


			System.out.println("********************************");


		}

		return result;
	}


	@Path("/system-information")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String fetchSystemInformation() {
		// Sigar API for fetching System information.
		Sigar sigar = new Sigar();
		JSONObject jsonObject = new JSONObject();
		DecimalFormat df = new DecimalFormat("#.##");

		try {
			CpuInfo[] cpuInfos = sigar.getCpuInfoList();
			double totalCpu = 0.0;
			for (int i = 0; i < cpuInfos.length; i++) {
				System.out.println(cpuInfos[i].getMhz());
				totalCpu += cpuInfos[i].getMhz();
			}

			jsonObject.put("cpuMax", df.format(totalCpu / 1000));
			//System.out.println(sigar.getCpuPerc().getCombined());
			OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

			System.out.println(osmb.getSystemCpuLoad());
			jsonObject.put("cpu", df.format(osmb.getSystemCpuLoad() * totalCpu / 1000));

			CpuTimer cpuTimer = new CpuTimer(sigar);

			System.out.println(cpuTimer.getCpuTotal());
			System.out.println(cpuTimer.getCpuUsage());

			//System.out.println(sigar.getCpu());
			System.out.println(sigar.getMem().getTotal());
			jsonObject.put("ramMax", df.format(sigar.getMem().getTotal() / 1048576));

			System.out.println(sigar.getMem().getUsed());
			jsonObject.put("ram", df.format(sigar.getMem().getUsed() / 1048576));

			FileSystem[] fileSystems = sigar.getFileSystemList();
			if(fileSystems[0] != null) {
				FileSystemUsage usage = sigar.getFileSystemUsage(fileSystems[0].getDirName());
				System.out.println(usage.getTotal());
				jsonObject.put("hdMax", df.format(usage.getTotal() / 1024));

				System.out.println(usage.getUsed());
				jsonObject.put("hd", df.format(usage.getUsed() / 1024));

			}

		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject.toJSONString();
	}


	// Below are all graph related APIs

	@GET
	@Path("/graphs/reqNoReqTypeRespTime")
	public String getReqNoReqTypeRespTime(@QueryParam ("algoIdentifier") String algoIdentifier){

		JSONObject jo = new JSONObject();
		JSONArray jArr = new JSONArray();
		ArrayList lst = new ArrayList();

		if(algoIdentifier.equals("1")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = HoneyBeeAlgorithm.getInstance().getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=HoneyBeeAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(3));
				}
				jArr.add(lst);
			}
		}else if(algoIdentifier.equals("2")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = AntAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=AntAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(3));
				}
				jArr.add(lst);
			}
		}else if(algoIdentifier.equals("3")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = LocationAwareAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=LocationAwareAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(3));
				}
				jArr.add(lst);
			}
		}else if(algoIdentifier.equals("4")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = PSOAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=PSOAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(3));
				}
				jArr.add(lst);
			}
		}

		jo.put("ReqNoReqTypeRespTime", jArr);
		System.out.println("##reqNoReqTypeRespTime##: "+jo);
		return jo.toString();
	}

	@GET
	@Path("/graphs/reqNoReqTypeLoc")
	public String getReqNoReqTypeLoc(@QueryParam ("algoIdentifier") String algoIdentifier){
		JSONObject jo = new JSONObject();
		JSONArray jArr = new JSONArray();
		ArrayList lst = new ArrayList();

		if(algoIdentifier.equals("1")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = HoneyBeeAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=HoneyBeeAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(0));
				}
				jArr.add(lst);
			}
		}else if(algoIdentifier.equals("2")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = AntAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=AntAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(0));
				}
				jArr.add(lst);
			}			
		}else if(algoIdentifier.equals("3")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = LocationAwareAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=LocationAwareAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(0));
				}
				jArr.add(lst);
			}			
		}else if(algoIdentifier.equals("4")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = PSOAlgorithm.getInstance().reqResTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=PSOAlgorithm.getInstance().reqResTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					lst = new ArrayList<>();
					lst.add(pair.getKey());
					lst.add(pair1.getKey());
					lst.add(al.get(0));
				}
				jArr.add(lst);
			}			
		}			

		jo.put("ReqNoReqTypeLoc", jArr);
		System.out.println("##reqNoReqTypeLoc##: "+jo);
		return jo.toString();
	}


	@GET
	@Path("/graphs/locReqTypeAvgResTime")
	public String getLocReqTypeAvgResTime(@QueryParam ("algoIdentifier") String algoIdentifier){
		JSONObject jo = new JSONObject();
		JSONArray jArr = new JSONArray();
		ArrayList lst = new ArrayList();

		if(algoIdentifier.equals("1")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = HoneyBeeAlgorithm.getInstance().locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=HoneyBeeAlgorithm.getInstance().locationAverageResponseTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				lst = new ArrayList<>();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					if(!al.isEmpty() && Double.parseDouble(al.get(0).toString())!=0){
						lst.add(pair.getKey());
						lst.add(pair1.getKey());
						lst.add(al.get(0));
					}
					if(!lst.isEmpty()){
						jArr.add(lst);
						lst = new ArrayList<>();
					}
				}
			}
		}else if(algoIdentifier.equals("2")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = AntAlgorithm.getInstance().locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=AntAlgorithm.getInstance().locationAverageResponseTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				lst = new ArrayList<>();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					if(!al.isEmpty() && Double.parseDouble(al.get(0).toString())!=0){
						lst.add(pair.getKey());
						lst.add(pair1.getKey());
						lst.add(al.get(0));
					}
					if(!lst.isEmpty()){
						jArr.add(lst);
						lst = new ArrayList<>();
					}
				}
			}
		}else if(algoIdentifier.equals("3")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = LocationAwareAlgorithm.getInstance().locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=LocationAwareAlgorithm.getInstance().locationAverageResponseTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				lst = new ArrayList<>();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					if(!al.isEmpty() && Double.parseDouble(al.get(0).toString())!=0){
						lst.add(pair.getKey());
						lst.add(pair1.getKey());
						lst.add(al.get(0));
					}
					if(!lst.isEmpty()){
						jArr.add(lst);
						lst = new ArrayList<>();
					}
				}
			}
		}else if(algoIdentifier.equals("4")){
			Iterator<Entry<Integer, HashMap<Integer, List>>> it = PSOAlgorithm.getInstance().locationAverageResponseTimeLogTable.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, HashMap<Integer, List>> pair = (Map.Entry<Integer, HashMap<Integer, List>>)it.next();
				HashMap hm=PSOAlgorithm.getInstance().locationAverageResponseTimeLogTable.get(pair.getKey());
				Iterator<Entry<Integer, HashMap<Integer, List>>> itr1 = hm.entrySet().iterator();
				lst = new ArrayList<>();
				while (itr1.hasNext()) {
					Map.Entry<Integer, HashMap<Integer, List>> pair1 = (Map.Entry<Integer, HashMap<Integer, List>>)itr1.next();
					ArrayList al = (ArrayList) hm.get(pair1.getKey());
					if(!al.isEmpty() && Double.parseDouble(al.get(0).toString())!=0){
						lst.add(pair.getKey());
						lst.add(pair1.getKey());
						lst.add(al.get(0));
					}
					if(!lst.isEmpty()){
						jArr.add(lst);
						lst = new ArrayList<>();
					}
				}
			}
		}

		jo.put("LocReqTypeAvgResTime", jArr);
		System.out.println("##locReqTypeAvgResTime##: "+jo);
		return jo.toString();
	}
	
	//cost graphs
	@GET
	@Path("/graphs/reqNoCost")
	public String getReqNoCost(@QueryParam ("algoIdentifier") String algoIdentifier, @QueryParam ("reqNo") String reqNo){
		JSONObject jo = new JSONObject();
		JSONArray jArr = new JSONArray();
		ArrayList lst = new ArrayList();

		if(algoIdentifier.equals("1")){
			System.out.println("in algo = 1");
			Iterator<Entry<Integer, Double>> it = HoneyBeeAlgorithm.getInstance().getReqCost().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
				if(pair.getKey()==Integer.parseInt(reqNo)){
					jArr.add(pair.getValue());
					break;
				}
			}
		}else if(algoIdentifier.equals("2")){
			System.out.println("in algo = 2");
			Iterator<Entry<Integer, Double>> it = AntAlgorithm.getInstance().getReqCost().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
				if(pair.getKey()==Integer.parseInt(reqNo)){
					jArr.add(pair.getValue());
					break;
				}
			}
		}else if(algoIdentifier.equals("3")){
			System.out.println("in algo = 3");
			Iterator<Entry<Integer, Double>> it = LocationAwareAlgorithm.getInstance().getReqCost().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
				if(pair.getKey()==Integer.parseInt(reqNo)){
					jArr.add(pair.getValue());
					break;
				}
			}
		}else if(algoIdentifier.equals("4")){
			System.out.println("in algo = 4");
			Iterator<Entry<Integer, Double>> it = PSOAlgorithm.getInstance().getReqCost().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Double> pair = (Map.Entry<Integer, Double>)it.next();
				if(pair.getKey()==Integer.parseInt(reqNo)){
					jArr.add(pair.getValue());
					break;
				}
			}
		}			

		jo.put("ReqNoCost", jArr);
		System.out.println("##reqNoCost##: "+jo);
		return jo.toString();
	}
	

}
