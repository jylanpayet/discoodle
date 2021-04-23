package com.discoodle.api.controller;

import com.discoodle.api.model.*;
import com.discoodle.api.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/room")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping( "/addNewRoom")
    public Room addNewRoom(@RequestBody Room.RoomRequest request) {
        return roomService.createNewRoom(request.getRoom_name(), request.getRoom_members());
    }

    @PostMapping("/addNewMember/{room_id}")
    public void addNewMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        roomService.addNewMember(room_id, user_id);
    }

    @DeleteMapping("/removeMember/{room_id}")
    public Optional<Room> removeMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        return roomService.removeMember(room_id, user_id);
    }

    @PostMapping("/changeLinkPicture/{room_id}")
    public Optional<Room> changeLinkPicture(@PathVariable String room_id, @RequestBody Room.RoomRequest request) {
        return roomService.changeLinkPicture(room_id, request.getLink_picture());
    }

    @PostMapping("/changeAdmin/{room_id}")
    public Optional<Room> changeAdmin(@PathVariable String room_id, @RequestParam(value = "admin") Long room_admin) {
        return roomService.changeAdmin(room_id, room_admin);
    }

    @PutMapping("/renameRoom/{room_id}")
    public void renameRoom(@PathVariable String room_id, @RequestParam(value = "new_name") String new_name) {
        roomService.renameRoom(room_id, new_name);
    }

}
