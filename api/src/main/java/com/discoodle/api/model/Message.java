package com.discoodle.api.model;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Message {

	private final UUID convUUID;
	private final Integer id;
	private String content;
	private String sender;
	private String messageDate;
	private boolean pinned;

	private final String type;


	public Message(Integer id, String content, String sender, String messageDate, boolean pinned, UUID convUUID) {
		this.id = id;
		this.content = content;
		this.sender = sender;
		this.messageDate = messageDate;
		this.pinned = pinned;
		this.convUUID = convUUID;
		this.type = "MESSAGE";
	}
}
