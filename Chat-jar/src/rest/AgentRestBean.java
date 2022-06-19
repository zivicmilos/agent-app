package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agentmanager.AgentManagerRemote;
import agents.AgentType;
import chatmanager.ChatManagerRemote;
import models.User;
import ws.WSChat;

@Stateless
@Path("/agents")
public class AgentRestBean implements AgentRest{
	
	@EJB 
	AgentManagerRemote agentManager;
	@EJB 
	ChatManagerRemote chatManager;
	@EJB
	private WSChat ws;
	
	@Override
	public void getAgentTypes() {
		String response = "AGENT_TYPES!";
		//List<AgentType> a = agentManager.getAgentTypes();
		List<User> aa = chatManager.getAdmins();
		
		for (AgentType at : agentManager.getAgentTypes()) {
			response += at.getName() + "|";
		}
		for (User u : chatManager.getAdmins()) {	
			ws.onMessage(u.getUsername(), response);
		}
	}
}
