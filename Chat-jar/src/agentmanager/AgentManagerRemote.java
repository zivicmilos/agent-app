package agentmanager;

import javax.ejb.Remote;

import agents.Agent;

@Remote
public interface AgentManagerRemote {
	public String startAgent(String name, String agentId);
	public Agent getAgentById(String agentId);
}
