package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import messagemanager.Performative;
import models.User;
import models.UserMessage;
import util.JNDILookup;
import ws.WSChat;

@Stateful
@Remote(IAgent.class)
public class ChatAgent extends Agent {

	private static final long serialVersionUID = 1L;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Chat Agent!");
	}

	// private List<String> chatClients = new ArrayList<String>();

	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
	}

	@Override
	public void handleMessage(ACLMessage message) {
		if (message.receivers.contains(agentId)) {
			String response = "";
			switch (message.performative) {
			case REGISTER:
				String username = (String) message.userArgs.get("username");
				String password = (String) message.userArgs.get("password");

				boolean result = chatManager.register(new User(username, password));

				response = "REGISTER!Registered: " + (result ? "Yes!" : "No!");
				break;
			case LOG_IN:
				username = (String) message.userArgs.get("username");
				password = (String) message.userArgs.get("password");
				result = chatManager.login(username, password);

				response = "LOG_IN!Logged in: " + (result ? "Yes!" : "No!");
				break;
			case GET_REGISTERED:
				response = "REGISTERED!";
				List<User> registeredUsers = chatManager.registeredUsers();
				for (User u : registeredUsers) {
					response += u.toString() + "|";
				}
				for (User u : chatManager.loggedInUsers()) {
					System.out.println(response);
					ws.onMessage(u.getUsername(), response);
				}

				break;
			case GET_LOGGEDIN:
				response = "LOGGEDIN!";
				List<User> loggedinUsers = chatManager.loggedInUsers();
				for (User u : loggedinUsers) {
					response += u.toString() + "|";
				}
				for (User u : chatManager.loggedInUsers()) {
					System.out.println(response);
					ws.onMessage(u.getUsername(), response);
				}

				break;
			case GET_MESSAGES:
				username = (String) message.userArgs.get("username");
				if (username.equals("all")) {
					for (User u : chatManager.registeredUsers()) {
						response = "MESSAGES!";
						List<UserMessage> userMessages = chatManager.getMessages(u.getUsername());
						for (UserMessage um : userMessages) {
							response += um.toString() + "|";
						}
						System.out.println(response);
						ws.onMessage(u.getUsername(), response);
					}
				} else {
					response = "MESSAGES!";
					List<UserMessage> userMessages = chatManager.getMessages(username);
					for (UserMessage um : userMessages) {
						response += um.toString() + "|";
					}
					System.out.println(response);
					ws.onMessage((String) message.userArgs.get("username"), response);
				}
				break;
			default:
				response = "ERROR!Option: " + message.performative + " does not exist.";
				break;
			}
			if (!(message.performative == Performative.GET_REGISTERED || message.performative == Performative.GET_LOGGEDIN || message.performative == Performative.GET_MESSAGES)) {
				System.out.println(response);
				ws.onMessage((String) message.userArgs.get("username"), response);
			}
		}
	}

}
