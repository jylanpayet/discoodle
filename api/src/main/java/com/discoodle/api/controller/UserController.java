package com.discoodle.api.controller;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(path = "api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "{username}", method = GET)
    @ResponseBody
    public Optional<User> getUserByUserName(@PathVariable String username) {
        return userService.getUserByUserName(username);
    }

    @RequestMapping(value = "/infos/{user_id}", method = GET)
    @ResponseBody
    public Optional<User> getUserByID(@PathVariable Integer user_id) {
        return userService.getUserByID(user_id);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userID}")
    public void deleteUser(@PathVariable("userID") Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping(path = "/seeAllRooms/{user_ID}")
    @ResponseBody
    public List<Room> findAllRoomsByUserID(@PathVariable Integer user_ID) {
        return userService.getUserByID(user_ID).get().getRooms();
    }

    @GetMapping(path = "/seeAllGroups/{user_ID}")
    @ResponseBody
    public List<Groups> findAllGroupsByUserID(@PathVariable Integer user_ID) {
        return userService.getUserByID(user_ID).get().getGroups();
    }

    @GetMapping(path = "/seeAllSubjects/{user_ID}")
    @ResponseBody
    public List<Groups> findAllSubjectsByUserID(@PathVariable Integer user_ID) {
        List<Groups> AllSubjects = userService.getUserByID(user_ID).get().getGroups();
        List<Groups> UserSubjects = new ArrayList<>();
        for (int i = 0; i < AllSubjects.size(); i++) {
            if (AllSubjects.get(i).getType().equals(Groups.TypeOfGroup.SUBJECTS)) {
                UserSubjects.add(AllSubjects.get(i));
            }
        }
        return UserSubjects;
    }
}