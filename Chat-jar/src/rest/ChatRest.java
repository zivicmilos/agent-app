package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import models.User;
import models.UserMessageDTO;

@Remote
public interface ChatRest {
	@POST
	@Path("/users/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public void register(User user);
	
	@POST
	@Path("/users/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public void login(User user);
	
	@GET
	@Path("/users/registered")
	public void getRegisteredUsers();
	
	@GET
	@Path("/users/loggedIn")
	public void getloggedInUsers();
	
	@DELETE
	@Path("/users/loggedIn/{user}")
	public void logoutUser(@PathParam("user") String user);
	
	@GET
	@Path("/messages/{user}")
	public void getMessages(@PathParam("user") String user);
	
	@POST
	@Path("/messages/user")
	@Consumes(MediaType.APPLICATION_JSON)
	public void send(UserMessageDTO dto);
	
	@POST
	@Path("/messages/all")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendToAll(UserMessageDTO dto);
}
