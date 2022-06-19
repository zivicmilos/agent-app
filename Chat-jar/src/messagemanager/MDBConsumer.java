package messagemanager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import agentmanager.AgentManagerRemote;
import agents.IAgent;
import agents.AID;
import agents.CachedAgentsRemote;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/publicTopic") })
public class MDBConsumer implements MessageListener {

	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private AgentManagerRemote agentManager;

	/**
	 * Default constructor.
	 */
	public MDBConsumer() {

	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		ACLMessage aclMessage = null;
		try {
			aclMessage = (ACLMessage) ((ObjectMessage) message).getObject();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		AID receiver;
		receiver = (AID) aclMessage.userArgs.get("receiver");
		IAgent agent = (IAgent) agentManager.getAgentById(receiver);
		agent.handleMessage(aclMessage);
	}

}
