package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import models.UserMessageDTO;

@Stateless
@Path("/chat")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;
	
	
	@Override
	public void register(User user) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "REGISTER");
		message.userArgs.put("username", user.getUsername());
		message.userArgs.put("password", user.getPassword());
		
		messageManager.post(message);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getRegisteredUsers();
	}

	@Override
	public void login(User user) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "LOG_IN");
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
	public void getRegisteredUsers() {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "GET_REGISTERED");
		
		messageManager.post(message);
	}

	@Override
	public void getloggedInUsers() {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "GET_LOGGEDIN");
		
		messageManager.post(message);
	}
	
	@Override
	public void logoutUser(String user) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", user);
		message.userArgs.put("command", "LOGOUT");
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
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", "chat");
		message.userArgs.put("command", "GET_MESSAGES");
		message.userArgs.put("username", user);
		
		messageManager.post(message);
	}

	@Override
	public void send(UserMessageDTO dto) {
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", dto.getSender());
		message.userArgs.put("command", "SEND_MESSAGE");
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
		AgentMessage message = new AgentMessage();
		message.userArgs.put("receiver", dto.getSender());
		message.userArgs.put("command", "SEND_MESSAGE_TO_ALL");
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
