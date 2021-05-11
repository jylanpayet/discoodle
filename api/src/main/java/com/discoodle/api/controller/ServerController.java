package com.discoodle.api.controller;

import com.discoodle.api.model.Room;
import com.discoodle.api.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/server")
public class ServerController {

    ServerService serverService;


    //The server is generated automaticaly during the creation of a groups.SUBJECTS.

    @PostMapping(path = "/addMember")
    public void addNewMember(@RequestParam(value="server_id") Long server_id, @RequestParam(value = "user_id") Long user_id) {
        serverService.addNewMember(server_id, user_id);
    }

    @PostMapping(path = "/addRoom")
    public Optional<Room> addNewRoom(@RequestParam(value="server_id")  Long server_id, @RequestParam(value = "name") String name) {
        return serverService.addNewRoom(server_id, name);
    }

    @GetMapping("/seeAllServRooms")
    @ResponseBody
    public List<Room> findAllRoomsByServerID(@RequestParam(value="server_id") Long server_id) {
        return serverService.findAllRoomsByServerID(server_id);
    }

}
