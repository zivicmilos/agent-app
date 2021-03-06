package agentmanager;

import java.util.List;

import javax.ejb.Remote;

import agents.AID;
import agents.AgentType;
import agents.IAgent;

@Remote
public interface AgentManagerRemote {
	public AID startAgent(String name, AID agentId);
	public IAgent getAgentById(AID agentId);
	public List<AgentType> getAgentTypes();
}
