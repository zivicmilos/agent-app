package models;

import java.io.Serializable;
import java.util.Objects;

public class AgentCenter implements Serializable {
	private static final long serialVersionUID = 1L;
	private String alias;
	private String address;
	
	public AgentCenter() {
		super();
	}

	public AgentCenter(String alias, String address) {
		super();
		this.alias = alias;
		this.address = address;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(address, alias);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentCenter other = (AgentCenter) obj;
		return alias.equals(other.alias);
	}
	
	
}
