package com.discoodle.api.controller;

import com.discoodle.api.model.GroupsRequest;
import com.discoodle.api.service.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class GroupsController {
    private final GroupsService groupsService;

    @PostMapping(path = "/api/groups/addNewGroup")
    public void addNewGroup(@RequestBody GroupsRequest request){
        groupsService.createNewGroup(request);
    }
}
