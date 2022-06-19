package agents;

import java.io.Serializable;

import messagemanager.ACLMessage;

public interface IAgent extends Serializable {

	public AID init(AID id);
	public void handleMessage(ACLMessage message);
	public AID getAgentId();
}
