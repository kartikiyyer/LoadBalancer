package load;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import algorithm.ant.AntAlgorithm;
import algorithm.ant.AntConstants;
import algorithm.honeybee.HoneyBeeAlgorithm;
import algorithm.honeybee.HoneyBeeConstants;

@Path("/")
public class RequestController {	
	
	int request=0;
	
	@Path("/request")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainText() {		
		return "hello";
	}
	
	@Path("/request")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String processRequestParameter(@FormParam("cpu") double cpu, @FormParam("storage") double storage, @FormParam("ram") double ram, @FormParam("time") double time, @FormParam ("algoIdentifier") int algoIdentifier,@FormParam("requestType") int requestType) {
		if(algoIdentifier==2){
			AntAlgorithm aa = AntAlgorithm.getInstance();
			aa.setCpu(cpu);
			aa.setHd(storage);
			aa.setRam(ram);
			//aa.printPheromoneTable();
			
			System.out.println("After request");	
			
			int location = aa.antBasedControl();
			System.out.println(AntConstants.getInstance().getDeltaPheromone());
			// Increase amount of resources allocated.
			AntConstants.getInstance().increaseLocationDetails(location, cpu, storage, ram);
			// Decrease amount of allocated resources.
			//AntConstants.decreaseLocationMaxDetails(location, cpu, storage, ram);
			
			System.out.println(location);	
			System.out.println(AntConstants.getInstance().getLocationCPU());
			System.out.println(AntConstants.getInstance().getLocationHD());
			System.out.println(AntConstants.getInstance().getLocationRAM());
			
			System.out.println(AntConstants.getInstance().getLocationMaxCPU());
			System.out.println(AntConstants.getInstance().getLocationMaxHD());
			System.out.println(AntConstants.getInstance().getLocationMaxRAM());
			System.out.println();
			aa.printPheromoneTable();
			
			request ++;
			
			int status = forwardRequest(AntConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);
			
			if(status == 200) {
				AntConstants.getInstance().increaseLocationRequestCount(location);
			}
		}
		else if(algoIdentifier==1){
			try{
				HoneyBeeAlgorithm hbAlgorithm=HoneyBeeAlgorithm.getInstance();
				int location=hbAlgorithm.processHoneyBeeAlgorithm(cpu,storage,ram,time,requestType);
				System.out.println("cpu "+HoneyBeeConstants.getInstance().getLocationCPU());
				System.out.println("@@@@@@@@@@@@@@");
				
				HoneyBeeAlgorithm.getInstance().setRequest(HoneyBeeAlgorithm.getInstance().getRequest()+1);
				request=HoneyBeeAlgorithm.getInstance().getRequest();
//				request++;
				System.out.println("request number: "+request);
				System.out.println("Sending on location: "+HoneyBeeConstants.getInstance().getLocations().get(location));
				//int status = forwardRequest(HoneyBeeConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time), algoIdentifier, requestType);
				int status=200; //TODO temporary..since no actual servers...later on uncomment above line and comment this line
				if(status == 200) {
					HoneyBeeAlgorithm.getInstance().processTimeLogForRequest(request,location,requestType);
					HoneyBeeConstants.getInstance().increaseLocationRequestCount(location);
				}
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
		
		if(algoIdentifier==2){
			AntAlgorithm aa = AntAlgorithm.getInstance();
			
			// Decrease amount of resources allocated.
			AntConstants.getInstance().decreaseLocationDetails(location, cpu, storage, ram);
			aa.increasePheromoneCountOfLocation(location);
			
			System.out.println("After response");	
			
			System.out.println(location);	
			System.out.println("Request: " + request);
			System.out.println(AntConstants.getInstance().getLocationCPU());
			System.out.println(AntConstants.getInstance().getLocationHD());
			System.out.println(AntConstants.getInstance().getLocationRAM());
			
			System.out.println(AntConstants.getInstance().getLocationMaxCPU());
			System.out.println(AntConstants.getInstance().getLocationMaxHD());
			System.out.println(AntConstants.getInstance().getLocationMaxRAM());
			System.out.println();
			aa.printPheromoneTable();
			
			AntConstants.getInstance().decreaseLocationRequestCount(location);
		}else if(algoIdentifier==1){
			
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
				
				System.out.println("After response");	
				
				System.out.println(location);	
				System.out.println("Request: " + request);
				System.out.println(HoneyBeeConstants.getInstance().getLocationCPU());
				System.out.println(HoneyBeeConstants.getInstance().getLocationHD());
				System.out.println(HoneyBeeConstants.getInstance().getLocationRAM());
				
				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxCPU());
				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxHD());
				System.out.println(HoneyBeeConstants.getInstance().getLocationMaxRAM());
				System.out.println("TimeLog: "+HoneyBeeAlgorithm.getInstance().locationResponseTimeLogTable);
				System.out.println();
//				HoneyBeeAlgorithm.getInstance().printFitnessTable();
				
				HoneyBeeConstants.getInstance().decreaseLocationRequestCount(location);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		
		// TODO: Send response to client. Need to figure out how will this be processed by client.
		
		return "";
	}
	
	
}
