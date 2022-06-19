package ws;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import models.AgentCenter;
import rest.AgentRest;
import util.JNDILookup;

@Singleton
@ServerEndpoint("/ws/{username}")
public class WSChat {
	@EJB
	AgentRest agentRest;
	
	private AgentManagerRemote agentManager = JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);
	private Map<String, Session> sessions = new HashMap<String, Session>();
	
	@OnOpen
	public void onOpen(@PathParam("username") String username, Session session) {
		System.out.println(username);
		sessions.put(username, session);
		try {
			AID aid = new AID(username, new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()) , new AgentType("user"));
			AID aid2 = new AID(username, new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()), new AgentType("chat"));
			if (!aid2.equals(new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()) , new AgentType("chat"))) && agentManager.getAgentById(aid) == null) {
				agentManager.startAgent(JNDILookup.UserAgentLookup, aid);
				agentRest.getRunningAgents();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
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
