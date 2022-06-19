package models;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private AgentCenter agentCenter;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AgentCenter getAgentCenter() {
		return agentCenter;
	}
	
	public void setAgentCenter(AgentCenter agentCenter) {
		this.agentCenter = agentCenter;
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, AgentCenter agentCenter) {
		this.username = username;
		this.password = password;
		this.agentCenter = agentCenter;
	}
	
	public User() {}

	@Override
	public String toString() {
		return username + "," + password + "," + agentCenter.getAlias() + "," + agentCenter.getAddress();
	}
	
}
