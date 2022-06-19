package agentmanager;

import javax.ejb.Remote;

import agents.AID;
import agents.IAgent;

@Remote
public interface AgentManagerRemote {
	public AID startAgent(String name, AID agentId);
	public IAgent getAgentById(AID agentId);
}
