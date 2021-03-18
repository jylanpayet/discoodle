package com.discoodle.api.controller;

import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.*;
import com.discoodle.api.service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {
	private final RoomService roomService;

	public RoomController(RoomService roomService) { this.roomService = roomService; }

	@PostMapping(path = "/api/room/addNewRoom")
	public void addNewRoom(@RequestBody RoomRequest request){
		roomService.createNewRoom(request);
	}

	@MessageMapping("/{roomUUID}/room.register")
	@SendTo("/conversations/rooms/{roomUUID}")
	public ChatMessage register(@DestinationVariable String roomUUID, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

	@MessageMapping("/{roomUUID}/room.addMembers")
	@SendTo("/conversations/rooms/{roomUUID}")
	public ChatMessage addMembers(List<User> users, @DestinationVariable String roomUUID) {
		return roomService.addMembers(users, roomUUID);
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


}
