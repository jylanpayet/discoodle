package com.discoodle.api.controller;

import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.*;
import com.discoodle.api.request.RoomRequest;
import com.discoodle.api.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class RoomController {
	private final RoomService roomService;


	@PostMapping(path = "/api/room/addNewRoom")
	public Room addNewRoom(@RequestBody RoomRequest request){
		return roomService.createNewRoom(request);
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

	@GetMapping("/api/findRoomById/{room_id}")
	public Optional<Room> findRoomById(@PathVariable("room_id") String room_id){
		return roomService.findRoomByID(room_id);
	}

	@GetMapping("/api/findAllRoomByName/{room_name}")
	public Optional<Room> findAllRoomByName(@PathVariable("room_name") String room_name){
		return roomService.findAllRoomByName(room_name);
	}

}
