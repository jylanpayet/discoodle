package com.discoodle.api.controller;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.request.RoomRequest;
import com.discoodle.api.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/room")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostMapping( "/addNewRoom")
    public Room addNewRoom(@RequestBody RoomRequest request) {
        return roomService.createNewRoom(request.getRoom_name(), request.getRoom_members());
    }

    @GetMapping("/findUserOfRoom")
    public List<User> findUserOfRoom(@RequestParam(value = "room_id") String room_id) {
        return roomService.findUserOfRoom(room_id);
    }

    @GetMapping("/findAdminOfRoom")
    public User findAdminOfRoom(@RequestParam(value = "room_id") String room_id) {
        Optional<User> temp = roomService.findAdminOfRoom(room_id);
        return temp.orElse(null);
    }

    @PostMapping("/addNewMember/{room_id}")
    public void addNewMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        roomService.addNewMember(room_id, user_id);
    }

    @DeleteMapping("/removeMember/{room_id}")
    public Optional<User> removeMember(@PathVariable String room_id, @RequestParam(value = "user_id") Long user_id) {
        return roomService.removeMember(room_id, user_id);
    }

    @PostMapping("/changeLinkPicture/{room_id}")
    public Optional<Room> changeLinkPicture(@PathVariable String room_id,@RequestParam(value="link_picture") String link_picture) {
        return roomService.changeLinkPicture(room_id,link_picture);
    }

    @PutMapping("/changeAdmin/{room_id}")
    public Optional<Room> changeAdmin(@PathVariable String room_id, @RequestParam(value = "admin") Long room_admin) {
        return roomService.changeAdmin(room_id, room_admin);
    }

    @PutMapping("/renameRoom/{room_id}")
    public void renameRoom(@PathVariable String room_id, @RequestParam(value = "new_name") String new_name) {
        roomService.renameRoom(room_id, new_name);
    }

    @DeleteMapping("/removeRoom/{room_id}")
    public void deleteRoom(@PathVariable String room_id){
        if(roomRepository.existsById(room_id))
            roomRepository.deleteById(room_id);
    }
}
