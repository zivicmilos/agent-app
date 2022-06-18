package chatmanager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

import models.Host;
import models.User;
import models.UserMessage;

// TODO Implement the rest of Client-Server functionalities 
/**
 * Session Bean implementation class ChatBean
 */
@Stateful
@LocalBean
public class ChatManagerBean implements ChatManagerRemote, ChatManagerLocal {

	private static List<User> registered = new ArrayList<User>();
	private static List<User> loggedIn = new ArrayList<User>();
	private static HashMap<String, List<UserMessage>> messages = new HashMap<String, List<UserMessage>>();
	
	/**
	 * Default constructor.
	 */
	public ChatManagerBean() {
		/*List<UserMessage> lista = new ArrayList<>();
		lista.add(new UserMessage(new User("asd", "asd"), new User("pera", "pera"), LocalDateTime.now(), "poruka", "admebloksam"));
		messages.put("asd", lista);*/
	}

	@Override
	public boolean register(User user) {
		try {
			user.setHost(new Host(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		registered.add(user);
		messages.put(user.getUsername(), new ArrayList<UserMessage>());
		try {
			System.out.println(InetAddress.getLocalHost().getHostName());
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean login(String username, String password) {
		boolean exists = registered.stream().anyMatch(u->u.getUsername().equals(username) && u.getPassword().equals(password));
		if(exists) {
			User user = registered.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
			loggedIn.add(new User(username, password, new Host(user.getHost().getAlias(), user.getHost().getAddress())));
		}
		return exists;
	}
	
	@Override
	public List<User> registeredUsers() {
		return registered;
	}

	@Override
	public List<User> loggedInUsers() {
		return loggedIn;
	}
	
	@Override
	public boolean logout(String username) {
		return loggedIn.removeIf(u -> u.getUsername().equals(username));
	}
	
	@Override
	public List<UserMessage> getMessages(String username) {
		return messages.get(username);
	}

	@Override
	public boolean send(String receiver, String sender, String subject, String content) {
		List<UserMessage> userMessages = messages.get(receiver);
		UserMessage userMessage = new UserMessage(new User(receiver), new User(sender), LocalDateTime.now(), subject, content);
		userMessages.add(userMessage);
		messages.put(userMessage.getReceiver().getUsername(), userMessages);
		return true;
	}

	@Override
	public boolean sendToAll(String sender, String subject, String content) {
		for (Map.Entry<String, List<UserMessage>> entry : messages.entrySet()) {
		    String key = entry.getKey();
		    List<UserMessage> value = entry.getValue();
		    if (!sender.equals(key)) {
		    	UserMessage userMessage = new UserMessage(new User(key), new User(sender), LocalDateTime.now(), subject, content);
				value.add(userMessage);
				messages.put(userMessage.getReceiver().getUsername(), value);
		    }
		}
		return true;
	}

}
