package agents;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import models.User;
import ws.WSChat;

@Stateful
@Remote(IAgent.class)
public class UserAgent extends Agent {

	private static final long serialVersionUID = 1L;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}

	@Override
	public void handleMessage(ACLMessage message) {
		if (message.receivers.contains(agentId)) {
			String response = "";
			switch (message.performative) {
			case LOGOUT:
				System.out.println("Logged users:");
				for (User u : chatManager.loggedInUsers())
					System.out.println(u.getUsername());
				String username = (String) message.userArgs.get("username");
				boolean result = chatManager.logout(username);

				response = "LOGGEDOUT!Logged out: " + (result ? "Yes!" : "No!");

				break;
			case SEND_MESSAGE:
				String messageReceiver = (String) message.userArgs.get("messageReceiver");
				String messageSender = (String) message.userArgs.get("messageSender");
				String messageSubject = (String) message.userArgs.get("messageSubject");
				String messageContent = (String) message.userArgs.get("messageContent");

				result = chatManager.send(messageReceiver, messageSender, messageSubject, messageContent);

				response = "SENT!Message sent: " + (result ? "Yes!" : "No!");

				break;
			case SEND_MESSAGE_TO_ALL:
				messageSender = (String) message.userArgs.get("messageSender");
				messageSubject = (String) message.userArgs.get("messageSubject");
				messageContent = (String) message.userArgs.get("messageContent");

				result = chatManager.sendToAll(messageSender, messageSubject, messageContent);

				response = "SENT_TO_ALL!Message sent to all: " + (result ? "Yes!" : "No!");

				break;
			default:
				response = "ERROR!Option: " + message.performative + " does not exist.";
				break;
			}
			System.out.println(response);
			ws.onMessage((String) message.userArgs.get("username"), response);
		}
	}
}
