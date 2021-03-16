package com.discoodle.api.model;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessage {

	private final UUID convUUID;
	private Integer senderID;
	private String sender;
	private String messageDate;
	private boolean pinned;
	private final UUID convUUID;

	private final String type;


	public ChatMessage(Integer ID, String content, String sender, String messageDate, boolean pinned, UUID convUUID) {
		this.ID = ID;
		this.content = content;
		this.sender = sender;
		this.messageDate = messageDate;
		this.pinned = pinned;
		this.convUUID = convUUID;
		this.type = "MESSAGE";
	}
}
