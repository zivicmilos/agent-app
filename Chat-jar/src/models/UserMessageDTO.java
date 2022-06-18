package models;

import java.io.Serializable;

public class UserMessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sender;
	private String receiver;
	private String subject;
	private String content;
	
	public UserMessageDTO() {
		super();
	}

	public UserMessageDTO(String sender, String receiver, String subject, String content) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return sender + "," + receiver + "," + subject + "," + content;
	}
}
