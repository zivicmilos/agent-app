package agents;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 * Session Bean implementation class CachedAgents
 */
@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	HashMap<AID, IAgent> runningAgents;

	/**
	 * Default constructor.
	 */
	public CachedAgents() {

		runningAgents = new HashMap<>();
	}

	@Override
	public HashMap<AID, IAgent> getRunningAgents() {
		return runningAgents;
	}

	@Override
	public void addRunningAgent(AID key, IAgent agent) {
		runningAgents.put(key, agent);
	}

}
