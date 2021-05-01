package com.discoodle.api.controller;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.request.RegistrationRequest;
import com.discoodle.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/findByUserName")
    @ResponseBody
    public Optional<User> getUserByUserName(@RequestParam("username") String username) {
        return userService.getUserByUserName(username);
    }

    @GetMapping("/infos")
    @ResponseBody
    public Optional<User> getUserByID(@RequestParam("user_id") Long user_id) {
        return userService.getUserByID(user_id);
    }

    @GetMapping("/info/{role}")
    @ResponseBody
    public Optional<User.Role> getUserByRole(@PathVariable User.Role role) {
        return userService.getUserByRole(role);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam("user_id") Long user_id) {
        userService.deleteUser(user_id);
    }

    @GetMapping("/seeAllRooms")
    @ResponseBody
    public List<Room> getAllRoomsByUserID(@RequestParam("user_id") Long user_id) {
        List<Room> all = userService.getUserByID(user_id).get().getRooms();
        List<Room> rooms = new ArrayList<>();
        for (Room room : all) {
            if (!room.getRoom_link())
                rooms.add(room);
        }
        return rooms;
    }

    @GetMapping("/seeAllFriends")
    @ResponseBody
    public List<User> getFriendList(@RequestParam("user_id") Long user_id) {
        return userService.getFriendList(user_id);
    }

    @PostMapping("/changeUsername")
    public Optional<User> changeUsername(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changeUsername(user_id, request.getUsername());
    }

    @PostMapping("/changeMail")
    public Optional<User> changeMail(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changeMail(user_id, request.getMail());
    }

    @PostMapping("/changePassword")
    public Optional<User> changePassword(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changePassword(user_id, request.getPassword());
    }

    @PostMapping("/changeName")
    public Optional<User> changeName(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changeName(user_id, request.getName());
    }

    @PostMapping("/changeLastName")
    public Optional<User> changeLastName(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changeLastName(user_id, request.getLast_name());
    }

    @PostMapping("/changeLinkToAvatar")
    public Optional<User> changeLinkToAvatar(@RequestParam("user_id") Long user_id, @RequestBody RegistrationRequest request) {
        return userService.changeLinkToAvatar(user_id, request.getLink_to_avatar());
    }

    @GetMapping("/seeAllGroups")
    @ResponseBody
    public List<Groups> getAllGroupsByUserID(@RequestParam("user_id") Long user_id) {
        return userService.getUserByID(user_id).get().getGroups();
    }

    @GetMapping("/seeAllSubjects")
    @ResponseBody
    public List<Groups> getAllSubjectsByUserID(@RequestParam("user_id") Long user_id) {
        List<Groups> AllSubjects = userService.getUserByID(user_id).get().getGroups();
        List<Groups> UserSubjects = new ArrayList<>();
        for (Groups allSubject : AllSubjects) {
            if (allSubject.getType().equals(Groups.TypeOfGroup.SUBJECTS)) {
                UserSubjects.add(allSubject);
            }
        }
        return UserSubjects;
    }

}
