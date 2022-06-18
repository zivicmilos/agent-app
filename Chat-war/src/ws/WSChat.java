package ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import util.JNDILookup;

@Singleton
@ServerEndpoint("/ws/{username}")
public class WSChat {
	private AgentManagerRemote agentManager = JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	private Map<String, Session> sessions = new HashMap<String, Session>();
	
	@OnOpen
	public void onOpen(@PathParam("username") String username, Session session) {
		System.out.println(username);
		sessions.put(username, session);
		if (!username.equals("chat") && agentManager.getAgentById(username) == null)
			agentManager.startAgent(JNDILookup.UserAgentLookup, username);
	}
	
	@OnClose
	public void onClose(@PathParam("username") String username, Session session) {
		sessions.remove(username);
	}
	
	@OnError
	public void onError(@PathParam("username") String username, Session session, Throwable t) {
		sessions.remove(username);
		t.printStackTrace();
	}
	
	public void onMessage(String username, String message) {
		System.out.println("Sesije:");
		for (String key : sessions.keySet()) {
		    System.out.println(key);
		}
		System.out.println("Korisnik "+username);
		Session session = sessions.get(username);
		if (session == null) System.out.println("nemaaaa");
		sendMessage(session, message);
	}
	
	public void onMessage(String message) {
		sessions.values().forEach(session -> sendMessage(session, message));
	}
	
	public void sendMessage(Session session, String message) {
		if(session.isOpen()) {
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
