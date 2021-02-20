package com.discoodle.api.model;
import java.util.UUID;

public class ChatMessage {

	private final Integer ID;
	private String content;
	private String sender;
	private String messageDate;
	private boolean pinned;
	private final UUID convUUID;

	public ChatMessage(Integer ID, String content, String sender, String messageDate, boolean pinned, UUID convUUID) {
		this.ID = ID;
		this.content = content;
		this.sender = sender;
		this.messageDate = messageDate;
		this.pinned = pinned;
		this.convUUID = convUUID;
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

	public UUID getConvUUID() {
		return convUUID;
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
