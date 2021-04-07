package com.discoodle.api.controller;

import com.discoodle.api.model.Room;
import com.discoodle.api.model.Server;
import com.discoodle.api.service.ServerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class ServerController {
    ServerService serverService;

    //The server is generated automaticaly during the creation of a groups.SUBJECTS.

    @PostMapping(path = "/api/server/{server_id}/server.addMember")
    public void addNewMember(@PathVariable Long server_id, @RequestParam(value = "user_id") Long user_id){
        serverService.addNewMember(server_id,user_id);
    }

    @PostMapping(path = "/api/server/{server_id}/server.addRoom")
    public void addNewRoom(@PathVariable Long server_id,@RequestParam(value="name") String name){
        serverService.addNewRoom(server_id,name);
    }
}
