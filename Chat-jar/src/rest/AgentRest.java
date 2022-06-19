package rest;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Remote
public interface AgentRest {
	@GET
	@Path("/classes")
	public void getAgentTypes();
}
