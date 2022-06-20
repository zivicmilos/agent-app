package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import messagemanager.ACLMessage;

@Remote
public interface MessageRest {
	@GET
	public void getPerformatives(@HeaderParam("user-agent") String username);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendMessage(ACLMessage message);
}
