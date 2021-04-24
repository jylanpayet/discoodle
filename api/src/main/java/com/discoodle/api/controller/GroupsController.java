package com.discoodle.api.controller;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Roles;
import com.discoodle.api.model.Server;
import com.discoodle.api.repository.GroupsRepository;
import com.discoodle.api.request.EditGroupRequest;
import com.discoodle.api.request.GroupsRequest;
import com.discoodle.api.service.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
public class GroupsController {

    private final GroupsService groupsService;


    @PostMapping("/api/groups/addNewGroup")
    public Optional<Groups> addNewGroup(@RequestBody GroupsRequest request) {
        return groupsService.createNewGroup(request);
    }

    @DeleteMapping("/api/groups/deleteGroupById/{groups_id}")
    public void deleteGroupByID(@PathVariable("groups_id") Long groups_ID) {
        groupsService.deleteGroupByID(groups_ID);
    }

    @PostMapping("/api/groups/editGroup")
    public void editGroup(@RequestBody EditGroupRequest request) {
        groupsService.editGroup(request);
    }

    @GetMapping( "/api/groups/getGroupByID/{group_id}")
    public Optional<Groups> getGroupByID(@PathVariable("group_id") Long group_id) {
        return groupsService.findGroupsByID(group_id);
    }

    @PostMapping( "/api/groups/addNewMemberInGroup/{groups_id}")
    public Optional<Groups> addNewMemberInGroup(@PathVariable Long groups_id, @RequestParam(value = "user_id") Long user_id, @RequestParam(value = "token") String token) {
        return groupsService.addNewMemberInGroup(groups_id, user_id, token);
    }


    @GetMapping( "/api/groups/serverGroup/{groups_id}")
    public Optional<Server> serverOfGroup(@PathVariable("groups_id") Long groups_ID) {
        return groupsService.serverOfGroup(groups_ID);
    }

    @GetMapping( "/api/groups/underGroup/{groups_id}")
    public List<Groups> underGroup(@PathVariable("groups_id") Long groups_ID) {
        return groupsService.underGroup(groups_ID);
    }

    @GetMapping("/getAllRolesByGroup/{group_id}")
    public List<Roles> getAllRolesByGroup(@PathVariable Long group_id) {
        return groupsService.findGroupsByID(group_id).get().getRoles();
    }

    @GetMapping("/getRoleByGroupAndUser/{group_id}/{user_id}")
    public List<Roles> getRoleByGroupAndUser(@PathVariable Long group_id, @PathVariable Long user_id) {
        return groupsService.getRoleByGroupAndUser(group_id, user_id);
    }

    @PostMapping("/addRoleForGroup/{group_id}")
    public Optional<Roles> addRoleForGroup(@PathVariable Long group_id, @RequestParam(value = "role_name") String role_name, @RequestParam(value = "rights") String rights) {
        return groupsService.addRoleForGroup(group_id, role_name, rights);
    }

    @PostMapping("/addRoleForUser/")
    public Optional<Roles> addRoleForUsers(@RequestParam(value = "users") List<Long> user_id, @RequestParam Long role_id) {
        return groupsService.addRoleForUsers(user_id, role_id);
    }

    @PutMapping("/modifyRightsForRole/{role_id}")
    public Optional<Roles> modifyRightsForRole(@PathVariable Long role_id, @RequestParam(value = "rights") String rights) {
        return groupsService.modifyRightsForRole(role_id, rights);
    }

    @DeleteMapping("/deleteRole/{role_id}")
    public Boolean deleteRole(@PathVariable Long role_id) {
        return groupsService.deleteRole(role_id);
    }

}
