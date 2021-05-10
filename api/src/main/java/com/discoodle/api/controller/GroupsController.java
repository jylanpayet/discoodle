package com.discoodle.api.controller;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Roles;
import com.discoodle.api.model.Server;
import com.discoodle.api.request.EditGroupRequest;
import com.discoodle.api.request.GroupsRequest;
import com.discoodle.api.service.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("api/groups")
public class GroupsController {

    private final GroupsService groupsService;


    @PostMapping("/addNewGroup")
    public Optional<Groups> addNewGroup(@RequestBody GroupsRequest request) {
        return groupsService.createNewGroup(request);
    }

    @DeleteMapping("/deleteGroupById")
    public void deleteGroupByID(@RequestParam("group_id") Long group_id) {
        groupsService.deleteGroupByID(group_id);
    }

    @PostMapping("/editGroup")
    public void editGroup(@RequestBody EditGroupRequest request) {
        groupsService.editGroup(request);
    }

    @GetMapping( "/getGroupByID")
    public Optional<Groups> getGroupByID(@RequestParam("group_id") Long group_id) {
        return groupsService.findGroupsByID(group_id);
    }

    @GetMapping("/findIDOfDiscoodle")
    public Optional<Long> findIDOfDiscoodle() {
        return groupsService.findIDOfDiscoodle();
    }

    @PostMapping( "/addNewMemberInGroup")
    public Optional<Groups> addNewMemberInGroup(@RequestParam("group_id") Long group_id, @RequestParam(value = "user_id") Long user_id, @RequestParam(value = "token") String token) {
        return groupsService.addNewMemberInGroup(group_id, user_id, token);
    }


    @GetMapping( "/serverGroup")
    public Optional<Server> serverOfGroup(@RequestParam("group_id") Long group_id) {
        return groupsService.serverOfGroup(group_id);
    }

    @GetMapping( "/underGroup")
    public List<Groups> underGroup(@RequestParam("group_id") Long group_id) {
        return groupsService.underGroup(group_id);
    }

    @GetMapping("/getAllRolesByGroup")
    public List<Roles> getAllRolesByGroup(@RequestParam("group_id") Long group_id) {
        return groupsService.findGroupsByID(group_id).get().getRoles();
    }

    @GetMapping("/getRoleByGroupAndUser")
    public List<Roles> getRoleByGroupAndUser(@RequestParam("group_id") Long group_id, @RequestParam("user_id") Long user_id) {
        return groupsService.getRoleByGroupAndUser(group_id, user_id);
    }

    @PostMapping("/addRoleForGroup")
    public Optional<Roles> addRoleForGroup(@RequestParam("group_id") Long group_id, @RequestParam(value = "role_name") String role_name, @RequestParam(value = "rights") String rights) {
        return groupsService.addRoleForGroup(group_id, role_name, rights);
    }

    @PostMapping("/addRoleForUser")
    public Optional<Roles> addRoleForUsers(@RequestParam(value = "users") List<Long> user_id, @RequestParam("role_id") Long role_id) {
        return groupsService.addRoleForUsers(user_id, role_id);
    }

    @PutMapping("/modifyRightsForRole")
    public Optional<Roles> modifyRightsForRole(@RequestParam("role_id") Long role_id, @RequestParam(value = "rights") String rights) {
        return groupsService.modifyRightsForRole(role_id, rights);
    }

    @DeleteMapping("/deleteRole")
    public Boolean deleteRole(@RequestParam("role_id") Long role_id) {
        return groupsService.deleteRole(role_id);
    }

    @DeleteMapping("/deleteUser")
    public Boolean deleteUser(@RequestParam("user_id") Long user_id,@RequestParam("group_id") Long group_id) {
        return groupsService.deleteUser(user_id,group_id);
    }

}
