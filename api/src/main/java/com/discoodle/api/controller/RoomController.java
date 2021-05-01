package com.discoodle.api.controller;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.request.RoomRequest;
import com.discoodle.api.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostMapping( "/addNewRoom")
    public Room addNewRoom(@RequestBody RoomRequest request) {
        return roomService.createNewRoom(request.getRoom_name(), request.getRoom_members());
    }

    @GetMapping("/findRoomByID")
    public Optional<Room> getRoomByID(@RequestParam("room_uuid") String room_uuid) {
        return roomService.findRoomByID(room_uuid);
    }

    @GetMapping("/findUserOfRoom")
    public List<User> getUserOfRoom(@RequestParam(value = "room_id") String room_id) {
        return roomService.getUserOfRoom(room_id);
    }

    @GetMapping("/findAdminOfRoom")
    public User getAdminOfRoom(@RequestParam(value = "room_id") String room_id) {
        Optional<User> temp = roomService.getAdminOfRoom(room_id);
        return temp.orElse(null);
    }

    @PostMapping("/addNewMember")
    public void addNewMember(@RequestParam(value = "room_id") String room_id, @RequestParam(value = "user_id") Long user_id) {
        roomService.addNewMember(room_id, user_id);
    }

    @DeleteMapping("/removeMember")
    public Optional<User> removeMember(@RequestParam(value = "room_id") String room_id, @RequestParam(value = "user_id") Long user_id) {
        return roomService.removeMember(room_id, user_id);
    }

    @PostMapping("/changeLinkPicture")
    public Optional<Room> changeLinkPicture(@RequestParam(value = "room_id") String room_id,@RequestParam(value="link_picture") String link_picture) {
        return roomService.changeLinkPicture(room_id,link_picture);
    }

    @PutMapping("/changeAdmin")
    public Optional<Room> changeAdmin(@RequestParam(value = "room_id") String room_id, @RequestParam(value = "admin") Long room_admin) {
        return roomService.changeAdmin(room_id, room_admin);
    }

    @PutMapping("/renameRoom")
    public void renameRoom(@RequestParam(value = "room_id") String room_id, @RequestParam(value = "new_name") String new_name) {
        roomService.renameRoom(room_id, new_name);
    }

    @DeleteMapping("/removeRoom")
    public void deleteRoom(@RequestParam(value = "room_id") String room_id){
        if(roomRepository.existsById(room_id))
            roomRepository.deleteById(room_id);
    }
}
