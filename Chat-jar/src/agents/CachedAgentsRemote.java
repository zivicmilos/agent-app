package agents;

import java.util.HashMap;

public interface CachedAgentsRemote {

	public HashMap<AID, IAgent> getRunningAgents();
	public void addRunningAgent(AID key, IAgent agent);
	public void stopAgent(AID aid);
}
