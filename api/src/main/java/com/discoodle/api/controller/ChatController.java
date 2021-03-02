package com.discoodle.api.controller;

import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@MessageMapping("/{roomUUID}/room.register")
	@SendTo("/conversations/rooms/{roomUUID}")
	public ChatMessage register(@DestinationVariable String roomUUID, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

	@MessageMapping("/{roomUUID}/room.send")
	@SendTo("/conversations/rooms/{roomUUID}")
	public ChatMessage sendMessage(@DestinationVariable String roomUUID, @Payload ChatMessage chatMessage) {
		try{
			DiscoodleJsonFileWriter.runWriter(chatMessage, roomUUID);
		}catch (Exception e){
			e.printStackTrace();
		}
		return chatMessage;
	}
	@MessageMapping("/{roomUUID}/room.writing")
	@SendTo("/conversations/rooms/{roomUUID}")
	public ChatMessage sendWriting(@DestinationVariable String roomUUID, @Payload ChatMessage chatMessage ) {
		return chatMessage;
	}


}
