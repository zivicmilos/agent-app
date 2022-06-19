package messagemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agents.AID;

public class ACLMessage implements Serializable {
	public static final long serialVersionUID = 1L;
	public Performative performative;
	public AID sender;
	public List<AID> receivers;
	public AID replyTo;
	public String content;
	public Serializable contentObj;
	public Map<String, Serializable> userArgs;
	public String language;
	public String encoding;
	public String ontology;
	public String protocol;
	public String conversationId;
	public String replyWith;
	public String inReplyTo;
	public long replyBy;
	public ACLMessage() {
		super();
		this.receivers = new ArrayList<>();
		this.userArgs = new HashMap<>();
	}
	public ACLMessage(Performative performative, AID sender, List<AID> receivers, AID replyTo, String content,
			Serializable contentObj, Map<String, Serializable> userArgs, String language, String encoding,
			String ontology, String protocol, String conversationId, String replyWith, String inReplyTo, long replyBy) {
		super();
		this.performative = performative;
		this.sender = sender;
		this.receivers = receivers;
		this.replyTo = replyTo;
		this.content = content;
		this.contentObj = contentObj;
		this.userArgs = userArgs;
		this.language = language;
		this.encoding = encoding;
		this.ontology = ontology;
		this.protocol = protocol;
		this.conversationId = conversationId;
		this.replyWith = replyWith;
		this.inReplyTo = inReplyTo;
		this.replyBy = replyBy;
	}
}
