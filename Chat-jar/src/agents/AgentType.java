package agents;

import java.io.Serializable;
import java.util.Objects;

public class AgentType implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	
	public AgentType() {
		super();
	}
	
	public AgentType(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentType other = (AgentType) obj;
		return name.equals(other.name);
	}
	
	
}
