package agents;

import javax.ejb.EJB;

public abstract class Agent implements IAgent {
	private static final long serialVersionUID = 1L;
	@EJB
	private CachedAgentsRemote cachedAgents;
	protected AID agentId;

	@Override
	public AID init(AID id) {
		agentId = id;
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}

	@Override
	public AID getAgentId() {
		return agentId;
	}

}
