package com.discoodle.api.controller;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.configuration.DiscoodleJsonFileWriter;
import com.discoodle.api.model.*;
import com.discoodle.api.service.MessagesService;
import com.discoodle.api.service.RoomService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.io.FileReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

@AllArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;
    private final MessagesService messagesService;


    @PostMapping(path = "/api/room/addNewRoom")
    public Room addNewRoom(@RequestBody Room.RoomRequest request) {
        return roomService.createNewRoom(request.getRoom_name(), request.getRoom_members());
    }

    @PostMapping(path = "/api/room/{room_id}/room.add")
    public void addNewMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        roomService.addNewMember(room_id, user_id);
    }

    @DeleteMapping(path = "/api/room/{room_id}/room.delete")
    public Optional<Room> deleteMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        return roomService.deleteMember(room_id, user_id);
    }

    @PostMapping(path = "/api/room/changeLinkPicture/{room_id}")
    public Optional<Room> changeLinkPicture(@PathVariable String room_id, @RequestBody Room.RoomRequest request) {
        return roomService.changeLinkPicture(room_id, request.getLink_picture());
    }

    @PostMapping(path = "/api/room/changeAdmin/{room_id}")
    public Optional<Room> changeAdmin(@PathVariable String room_id, @RequestBody Room.RoomRequest request) {
        return roomService.changeAdmin(room_id, request.getRoom_admin());
    }

}
