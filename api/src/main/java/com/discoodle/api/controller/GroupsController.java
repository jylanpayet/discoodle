package com.discoodle.api.controller;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Server;
import com.discoodle.api.request.EditGroupRequest;
import com.discoodle.api.request.GroupRightsRequest;
import com.discoodle.api.request.GroupsRequest;
import com.discoodle.api.service.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
public class GroupsController {
    private final GroupsService groupsService;

    @PostMapping(path = "/api/groups/addNewGroup")
    public void addNewGroup(@RequestBody GroupsRequest request) {
        groupsService.createNewGroup(request);
    }

    @PostMapping(path = "/api/groups/editRights")
    public void editGroupRights(@RequestBody GroupRightsRequest request) {
        groupsService.editRights(request);
    }

    @DeleteMapping(path = "/api/groups/deleteGroupById/{groups_id}")
    public void deleteGroupByID(@PathVariable("groups_id") Long groups_ID) {
        groupsService.deleteGroupByID(groups_ID);
    }

    @PostMapping(path = "/api/groups/editGroup")
    public void editGroup(@RequestBody EditGroupRequest request){
        groupsService.editGroup(request);
    }

    @GetMapping(path ="/api/groups/getGroupByID/{group_id}")
    public Optional<Groups> getGroupByID(@PathVariable("group_id") Long group_id){
        return groupsService.findGroupsByID(group_id);
    }

    @PostMapping(path = "/api/groups/editFileGroup/{groups_id}")
    public void editFileGroup(@PathVariable("groups_id") Long groups_ID, @RequestBody GroupsRequest request){ groupsService.editFileGroup(groups_ID, request.getText()); }

    @PostMapping(path = "/api/groups/addNewMemberInGroup/{groups_id}")
    public void addNewMemberInGroup(@PathVariable Long groups_id, @RequestParam(value = "user_id") Long user_id, @RequestParam(value ="token" ) String token){ groupsService.addNewMemberInGroup(groups_id, user_id, token);}


    @GetMapping(path = "/api/groups/serverGroup/{groups_id}")
    public Server serverOfGroup(@PathVariable("groups_id") Long groups_ID) {
        return groupsService.serverOfGroup(groups_ID);
    }
}
