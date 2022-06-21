package rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import agents.CachedAgentsRemote;
import chatmanager.ChatManagerRemote;
import models.AgentCenter;
import models.User;
import util.JNDILookup;
import ws.WSChat;

@Stateless
@Path("/agents")
public class AgentRestBean implements AgentRest{
	
	@EJB 
	private AgentManagerRemote agentManager;
	@EJB 
	private CachedAgentsRemote cachedAgents;
	@EJB 
	private ChatManagerRemote chatManager;
	@EJB
	private WSChat ws;
	
	@Override
	public void getAgentTypes() {
		String response = "AGENT_TYPES!";
		
		for (AgentType at : agentManager.getAgentTypes()) {
			response += at.getName() + "|";
		}
		for (User u : chatManager.getLoggedInAdmins()) {	
			ws.onMessage(u.getUsername(), response);
		}
	}

	@Override
	public void getRunningAgents() {
		String response = "RUNNING_AGENTS!";
		boolean continueFlag = false;
		for (AID agentId : cachedAgents.getRunningAgents().keySet()) {
			continueFlag = false;
			for (User u : chatManager.getAdmins()) {
				if (u.getUsername().equals(agentId.getName())) {
					continueFlag = true;
					break;
				}
			}
			if (continueFlag) continue;
			
			response += agentId.getName() + "," + agentId.getAgentCenter().getAddress() + "," + 
					agentId.getAgentCenter().getAlias() + "," + agentId.getType().getName() + "|";
		}
		
		for (User u : chatManager.getLoggedInAdmins()) {	
			ws.onMessage(u.getUsername(), response);
		}
		
	}

	@Override
	public void runAgent(String type, String name, String username) {
		AID retVal = null;
		try {
			retVal = agentManager.startAgent(JNDILookup.UserAgentLookup, new AID(name, new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType(type)));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (retVal != null) {
			ws.onMessage(username, "RUN_AGENT!Agent ran: Yes!");
		}
		else {
			ws.onMessage(username, "RUN_AGENT!Agent ran: No!");
		}
		
		getAgentTypes();
		getRunningAgents();
	}

	@Override
	public void stopAgent(AID aid) {	
		cachedAgents.stopAgent(aid);
		
		getAgentTypes();
		getRunningAgents();
	}
}
