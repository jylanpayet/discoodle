package com.discoodle.api.model;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ChatMessage {

	private final UUID convUUID;
	private Integer senderID;
	private String sender;
	private String messageDate;
	private boolean pinned;
	private String content;


	public ChatMessage(UUID convUUID, Integer senderID, String sender, String messageDate, boolean pinned, String content) {
		this.convUUID = convUUID;
		this.senderID = senderID;
		this.sender = sender;
		this.messageDate = messageDate;
		this.pinned = pinned;
		this.content = content;
	}

}
