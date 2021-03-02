package com.discoodle.api.model;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

public class ChatMessage {

	private final Integer ID;
	private String content;
	private String sender;
	private String messageDate;
	private boolean pinned;
	private final UUID convUUID;

	@Enumerated(EnumType.STRING)
	private final Type type;


	enum Type{
		WRITING,
		SEND
	}

	public ChatMessage(Integer ID, String content, String sender, String messageDate, boolean pinned, UUID convUUID, Type type) {
		this.ID = ID;
		this.content = content;
		this.sender = sender;
		this.messageDate = messageDate;
		this.pinned = pinned;
		this.convUUID = convUUID;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}

	public Integer getId() {
		return ID;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public boolean isPinned() {
		return pinned;
	}

	public UUID getConvUUID() {
		return convUUID;
	}

	public Type getType() {
		return type;
	}

	public Integer getID() {
		return ID;
	}


	@Override
	public String toString() {
		return "ChatMessage{" +
				"ID=" + ID +
				", content='" + content + '\'' +
				", sender='" + sender + '\'' +
				", messageString=" + messageDate +
				", pinned=" + pinned +
				", convUUID=" + convUUID +
				'}';
	}
}
