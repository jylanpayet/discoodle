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
    public Optional<User> getUserByID(@PathVariable Long user_id){
        return userService.getUserByID(user_id);
    }

    @RequestMapping(value = "/info/{role}", method = GET)
    @ResponseBody
    public Optional<User.Role> findUserByRole(@PathVariable User.Role role){ return userService.findUserByRole(role); }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{user_id}")
    public void deleteUser(@PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping(path = "/seeAllRooms/{user_id}")
    @ResponseBody
    public List<Room> findAllRoomsByUserID(@PathVariable Long user_id) {
        return userService.getUserByID(user_id).get().getRooms();
    }

    @GetMapping(path = "/seeAllFriends/{user_id}")
    @ResponseBody
    public List<User> getFriendList(@PathVariable Long user_id) {
        return userService.getFriendList(user_id);
    }

    @PostMapping(path = "changeUsername/{user_id}")
    public Optional<User> changeUsername(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeUsername(user_id, request.getUsername());
    }

    @PostMapping(path = "changeMail/{user_id}")
    public Optional<User> changeMail(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeMail(user_id, request.getMail());
    }

    @PostMapping(path = "changePassword/{user_id}")
    public Optional<User> changePassword(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changePassword(user_id, request.getPassword());
    }

    @PostMapping(path = "changeName/{user_id}")
    public Optional<User> changeName(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeName(user_id, request.getName());
    }

    @PostMapping(path = "changeLastName/{user_id}")
    public Optional<User> changeLastName(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeLastName(user_id, request.getLast_name());
    }

    @PostMapping(path = "changeLinkToAvar/{user_id}")
    public Optional<User> changeLinkToAvatar(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeLinkToAvatar(user_id, request.getLink_to_avatar());
    }

    @GetMapping(path = "/seeAllGroups/{user_ID}")
    @ResponseBody
    public List<Groups> findAllGroupsByUserID(@PathVariable Long user_ID) {
        return userService.getUserByID(user_ID).get().getGroups();
    }

    @GetMapping(path = "/seeAllSubjects/{user_ID}")
    @ResponseBody
    public List<Groups> findAllSubjectsByUserID(@PathVariable Long user_ID) {
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
