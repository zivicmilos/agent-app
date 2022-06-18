package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagemanager.MessageManagerRemote;
import models.User;
import models.UserMessage;
import util.JNDILookup;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class ChatAgent implements Agent {

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
		System.out.println("Created Chat Agent!");
	}

	//private List<String> chatClients = new ArrayList<String>();

	protected MessageManagerRemote msm() {
		return (MessageManagerRemote) JNDILookup.lookUp(JNDILookup.MessageManagerLookup, MessageManagerRemote.class);
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
					case "REGISTER":
						String username = (String) tmsg.getObjectProperty("username");
						String password = (String) tmsg.getObjectProperty("password");
	
						boolean result = chatManager.register(new User(username, password));

						response = "REGISTER!Registered: " + (result ? "Yes!" : "No!");
						break;
					case "LOG_IN":
						username = (String) tmsg.getObjectProperty("username");
						password = (String) tmsg.getObjectProperty("password");
						result = chatManager.login(username, password);

						response = "LOG_IN!Logged in: " + (result ? "Yes!" : "No!");
						break;
					case "GET_REGISTERED":
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
					case "GET_LOGGEDIN":
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
					case "GET_MESSAGES":
						username = (String) tmsg.getObjectProperty("username");
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
						}
						else {
							response = "MESSAGES!";
							List<UserMessage> userMessages = chatManager.getMessages(username);
							for (UserMessage um : userMessages) {
								response += um.toString() + "|";
							}
							System.out.println(response);
							ws.onMessage((String) tmsg.getObjectProperty("username"), response);
						}
						break;
					case "x":
						break;
					default:
						response = "ERROR!Option: " + option + " does not exist.";
						break;
					}
					if (!(option.equals("GET_REGISTERED") || option.equals("GET_LOGGEDIN") || option.equals("GET_MESSAGES"))) {
						System.out.println(response);
						ws.onMessage((String) tmsg.getObjectProperty("username"), response);
					}
					
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
