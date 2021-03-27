package com.discoodle.api.controller;

import com.discoodle.api.model.GroupRightsRequest;
import com.discoodle.api.model.GroupsRequest;
import com.discoodle.api.service.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class GroupsController {
    private final GroupsService groupsService;

    @PostMapping(path = "/api/groups/addNewGroup")
    public void addNewGroup(@RequestBody GroupsRequest request){
        groupsService.createNewGroup(request);
    }


    @PostMapping(path = "/api/groups/editRights")
    public void editGroupRights(@RequestBody GroupRightsRequest request){
        groupsService.editRights(request);
    }

    @DeleteMapping(path = "/api/groups/deleteGroupById/{groups_id}")
    public void deleteGroupByID(@PathVariable("groups_id") Integer groups_ID){groupsService.deleteGroupByID(groups_ID);}
}
