package load;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import algorithm.ant.AntAlgorithm;
import algorithm.ant.AntConstants;

@Path("/hello")
public class RequestServer {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainText() {
		return "hello";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String processRequest(String message) {
		System.out.println(message);
		return "";
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String processRequestParameter(@FormParam("cpu") double cpu, @FormParam("hd") double hd, @FormParam("ram") double ram) {
		System.out.println(cpu);
		System.out.println(hd);
		System.out.println(ram);
		//AntAlgorithm aa = new AntAlgorithm(cpu, hd, ram);
		//aa.initializePheromoneTable(AntConstants.PHEROMONE);
		//aa.printPheromoneTable();
		return "";
	}
	
}
