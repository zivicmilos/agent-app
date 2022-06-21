package rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agents.AID;
import agents.AgentType;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import messagemanager.Performative;
import models.AgentCenter;
import models.User;
import models.UserMessageDTO;

@Stateless
@Path("/chat")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;
	
	
	@Override
	public void register(User user) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.REGISTER;
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		getRegisteredUsers();
	}

	@Override
	public void login(User user) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.LOG_IN;
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getloggedInUsers();
	}
	
	@Override
	public void adminLogin(User user) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.ADMIN_LOG_IN;
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
	}
	
	@Override
	public void getRegisteredUsers() {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.GET_REGISTERED;
		
		messageManager.post(message);
	}

	@Override
	public void getloggedInUsers() {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.GET_LOGGEDIN;
		
		messageManager.post(message);
	}
	
	@Override
	public void logoutUser(String user) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID(user, new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("user")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.LOGOUT;
		message.userArgs.put("username", user);
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getloggedInUsers();
	}

	@Override
	public void getMessages(String user) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.GET_MESSAGES;
		message.userArgs.put("username", user);
		
		messageManager.post(message);
	}

	@Override
	public void send(UserMessageDTO dto) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID(dto.getSender(), new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("user")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.SEND_MESSAGE;
		message.userArgs.put("messageSender", dto.getSender());
		message.userArgs.put("messageReceiver", dto.getReceiver());
		message.userArgs.put("messageSubject", dto.getSubject());
		message.userArgs.put("messageContent", dto.getContent());
		message.userArgs.put("username", dto.getSender());
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getMessages(dto.getReceiver());
		
	}

	@Override
	public void sendToAll(UserMessageDTO dto) {
		ACLMessage message = new ACLMessage();
		try {
			message.receivers.add(new AID(dto.getSender(), new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("user")));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		message.performative = Performative.SEND_MESSAGE_TO_ALL;
		message.userArgs.put("messageSender", dto.getSender());
		message.userArgs.put("messageSubject", dto.getSubject());
		message.userArgs.put("messageContent", dto.getContent());
		message.userArgs.put("username", dto.getSender());
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getMessages("all");
		
	}
}
