package com.discoodle.api.controller;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.*;
import com.discoodle.api.service.RoomService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

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

	@PutMapping(path = "/api/room/pinMessage/{roomUUID}")
	public void pinMessage(@RequestParam(value = "messageID") Integer messageID, @PathVariable String roomUUID) {
		Gson gson = new Gson();
		try {
			JsonReader reader = new JsonReader(new FileReader(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json"));
			LinkedList<Message> messages = gson.fromJson(reader, new TypeToken<LinkedList<Message>>(){}.getType());
			for (Message m : messages) {
				if (m.getId().equals(messageID)) {
					m.setPinned(true);
					Path path = Paths.get(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json");
					try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
						gson.toJson(gson.toJsonTree(messages), writer);
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PutMapping(path = "/api/room/unpinMessage/{roomUUID}")
	public void unpinMessage(@RequestParam(value = "messageID") Integer messageID, @PathVariable String roomUUID) {
		Gson gson = new Gson();
		try {
			JsonReader reader = new JsonReader(new FileReader(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json"));
			LinkedList<Message> messages = gson.fromJson(reader, new TypeToken<LinkedList<Message>>(){}.getType());
			for (Message m : messages) {
				if (m.getId().equals(messageID)) {
					m.setPinned(false);
					Path path = Paths.get(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json");
					try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
						gson.toJson(gson.toJsonTree(messages), writer);
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PutMapping(path = "/api/room/deleteMessage/{roomUUID}")
	public void deleteMessage(@RequestParam(value = "messageID") Integer messageID, @PathVariable String roomUUID) {
		Gson gson = new Gson();
		try {
			JsonReader reader = new JsonReader(new FileReader(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json"));
			LinkedList<Message> messages = gson.fromJson(reader, new TypeToken<LinkedList<Message>>(){}.getType());
			for (Message m : messages) {
				if (m.getId().equals(messageID)) {
					messages.remove(m);
					System.out.println(messages);
					Path path = Paths.get(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json");
					try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
						gson.toJson(gson.toJsonTree(messages), writer);
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Getter
	@AllArgsConstructor
	static class EditMessageRequest {
		private final Integer messageID;
		private final String content;
	}

	@PutMapping(path = "/api/room/editMessage/{roomUUID}")
	public void editMessage(@PathVariable String roomUUID, @RequestBody EditMessageRequest messageRequest) {
		Gson gson = new Gson();
		try {
			JsonReader reader = new JsonReader(new FileReader(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json"));
			LinkedList<Message> messages = gson.fromJson(reader, new TypeToken<LinkedList<Message>>(){}.getType());
			for (Message m : messages) {
				if (m.getId().equals(messageRequest.getMessageID())) {
					m.setContent(messageRequest.getContent());
					m.setEdited(true);
					Path path = Paths.get(ApiApplication.RESSOURCES + "static/common/json/"+roomUUID+".json");
					try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
						gson.toJson(gson.toJsonTree(messages), writer);
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
