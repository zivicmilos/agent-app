package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import ws.WSChat;

@Stateless
@Path("/messages")
public class MessageRestBean implements MessageRest {
	
	@EJB
	private MessageManagerRemote messageManager;
	@EJB
	private WSChat ws;

	@Override
	public void getPerformatives(String username) {
		String response = "PERFORMATIVES!";
		
		for (String p : messageManager.getPerformatives()) {
			response += p + "|";
		}
		
		ws.onMessage(username, response);
	}

	@Override
	public void sendMessage(ACLMessage message) {
		messageManager.post(message);
		
		ws.onMessage(message.sender.getName(), "MESSAGE_SENT! Message sent: Yes!");
	}

}
