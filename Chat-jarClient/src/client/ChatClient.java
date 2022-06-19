package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import agentmanager.AgentManagerBean;
import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import models.AgentCenter;
import util.JNDILookup;

public class ChatClient {

	private AgentManagerRemote agentManager = JNDILookup.lookUp(JNDILookup.AgentManagerLookup, AgentManagerBean.class);

	public void postConstruct() {
		try {
			agentManager.startAgent(JNDILookup.ChatAgentLookup, new AID("chat", new AgentCenter(InetAddress.getLocalHost().getHostName(), InetAddress.getLocalHost().getHostAddress()) , new AgentType("chat")));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public ChatClient() {
		postConstruct();
	}

	public static void main(String[] args) {
		new ChatClient();
	}

}
