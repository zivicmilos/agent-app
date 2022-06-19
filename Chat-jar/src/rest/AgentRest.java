package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import agents.AID;

@Remote
public interface AgentRest {
	@GET
	@Path("/classes")
	public void getAgentTypes();
	
	@GET
	@Path("/running")
	public void getRunningAgents();
	
	@PUT
	@Path("/running/{type}/{name}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void runAgent(@PathParam("type") String type, @PathParam("name") String name, String username);
	
	@DELETE
	@Path("/running")
	@Consumes(MediaType.APPLICATION_JSON)
	public void stopAgent(AID aid);
}
