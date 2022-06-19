package agents;

import java.io.Serializable;

import models.AgentCenter;

public class AID implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private AgentCenter agentCenter;
	private AgentType type;
	
	public AID() {
		super();
	}

	public AID(String name, AgentCenter agentCenter, AgentType type) {
		super();
		this.name = name;
		this.agentCenter = agentCenter;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentCenter getAgentCenter() {
		return agentCenter;
	}

	public void setAgentCenter(AgentCenter agentCenter) {
		this.agentCenter = agentCenter;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AID other = (AID) obj;
		return agentCenter.equals(other.agentCenter) && name.equals(other.name)
				&& type.equals(other.type);
	}
	
	
}
