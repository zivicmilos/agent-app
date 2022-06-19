package agentmanager;

import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.IAgent;
import agents.AID;
import agents.CachedAgentsRemote;
import util.JNDILookup;

/**
 * Session Bean implementation class AgentManagerBean
 */
@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	
    public AgentManagerBean() {
        
    }

	@Override
	public AID startAgent(String name, AID agentId) {
		IAgent agent = (IAgent) JNDILookup.lookUp(name, IAgent.class);
		return agent.init(agentId);
	}

	@Override
	public IAgent getAgentById(AID agentId) {
		for (Map.Entry<AID, IAgent> a : cachedAgents.getRunningAgents().entrySet())
			if (a.getKey().equals(agentId))
				return a.getValue();
		return null;
	}

}
