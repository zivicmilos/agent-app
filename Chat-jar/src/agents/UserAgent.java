package agents;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import models.User;
import ws.WSChat;


@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentId;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}

	@Override
	public void handleMessage(Message message) {
		TextMessage tmsg = (TextMessage) message;

		String receiver;
		try {
			receiver = (String) tmsg.getObjectProperty("receiver");
			if (agentId.equals(receiver)) {
				String option = "";
				String response = "";
				try {
					option = (String) tmsg.getObjectProperty("command");
					switch (option) {
					case "LOGOUT":
						System.out.println("Logged users:");
						for (User u : chatManager.loggedInUsers())
							System.out.println(u.getUsername());
						String username = (String) tmsg.getObjectProperty("username");
						boolean result = chatManager.logout(username);

						response = "LOGGEDOUT!Logged out: " + (result ? "Yes!" : "No!");

						break;
					case "SEND_MESSAGE":
						String messageReceiver = (String) tmsg.getObjectProperty("messageReceiver");
						String messageSender = (String) tmsg.getObjectProperty("messageSender");
						String messageSubject = (String) tmsg.getObjectProperty("messageSubject");
						String messageContent = (String) tmsg.getObjectProperty("messageContent");

						result = chatManager.send(messageReceiver, messageSender, messageSubject, messageContent);

						response = "SENT!Message sent: " + (result ? "Yes!" : "No!");

						break;
					case "SEND_MESSAGE_TO_ALL":
						messageSender = (String) tmsg.getObjectProperty("messageSender");
						messageSubject = (String) tmsg.getObjectProperty("messageSubject");
						messageContent = (String) tmsg.getObjectProperty("messageContent");

						result = chatManager.sendToAll(messageSender, messageSubject, messageContent);

						response = "SENT_TO_ALL!Message sent to all: " + (result ? "Yes!" : "No!");

						break;
					case "x":
						break;
					default:
						response = "ERROR!Option: " + option + " does not exist.";
						break;
					}
					System.out.println(response);
					ws.onMessage((String) tmsg.getObjectProperty("username"), response);

				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String init(String id) {
		agentId = id;
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}

	@Override
	public String getAgentId() {
		return agentId;
	}
}
