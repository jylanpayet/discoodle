package com.discoodle.api.controller;

import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.*;
import com.discoodle.api.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class RoomController {
	private final RoomService roomService;


	@PostMapping(path = "/api/room/addNewRoom")
	public Room addNewRoom(@RequestBody Room.RoomRequest request){
		return roomService.createNewRoom(request.getRoom_name(), request.getRoom_members());
	}

	@PostMapping(path = "/api/room/{room_id}/room.add")
	public Optional<Room> addNewMembers(@PathVariable String room_id, @RequestBody Room.RoomRequest request){
		return roomService.addNewMembers(room_id, request.getRoom_members());
	}

	@MessageMapping("/{roomUUID}/room.send")
	@SendTo("/conversations/rooms/{roomUUID}")
	public Message sendMessage(@DestinationVariable String roomUUID, @Payload Message message) {
		try{
			DiscoodleJsonFileWriter.runWriter(message, roomUUID);
		}catch (Exception e){
			e.printStackTrace();
		}
		return message;
	}


}
