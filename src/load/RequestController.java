package load;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import algorithm.ant.AntAlgorithm;
import algorithm.ant.AntConstants;

@Path("/")
public class RequestController {	
	
	int request = 0;
	
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
	public String processRequestParameter(@FormParam("cpu") double cpu, @FormParam("storage") double storage, @FormParam("ram") double ram, @FormParam("time") double time) {
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
		
		int status = forwardRequest(AntConstants.getInstance().getLocations().get(location), String.valueOf(location), String.valueOf(request), String.valueOf(cpu), String.valueOf(storage), String.valueOf(ram), String.valueOf(time));
		
		if(status == 200) {
			AntConstants.getInstance().increaseLocationRequestCount(location);
		}
		
		return "";
	}
	
	
	public int forwardRequest(String location, String locationId, String request, String cpu, String storage, String ram, String time) {
		String url = "http://"+location+":8080/Instance/request";
		String charset = "UTF-8";
		int status = 0;
		
		try {
			String query = String.format("server=%s&location=%s&request=%s&cpu=%s&storage=%s&ram=%s&time=%s", 
				URLEncoder.encode(AntConstants.getInstance().getServer(), charset),
				URLEncoder.encode(locationId, charset),
				URLEncoder.encode(request, charset),
				URLEncoder.encode(cpu, charset), 
			    URLEncoder.encode(storage, charset),
			    URLEncoder.encode(ram, charset),
			    URLEncoder.encode(time, charset));
			
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
			
			//HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			
			status = connection.getResponseCode();
			
			System.out.println("This is the status from server: "+ status);
			
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
	public String processResponseParameter(@FormParam("location") int location, @FormParam("request") int request, @FormParam("cpu") double cpu, @FormParam("storage") double storage, @FormParam("ram") double ram) {
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
		
		// TODO: Send response to client. Need to figure out how will this be processed by client.
		
		return "";
	}
	
	
}
