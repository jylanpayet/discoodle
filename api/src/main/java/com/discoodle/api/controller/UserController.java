package com.discoodle.api.controller;

import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "{username}", method = GET)
    @ResponseBody
    public Optional<User> getUserByUserName(@PathVariable String username){
        return userService.getUserByUserName(username);
    }

    @RequestMapping(value = "/infos/{user_id}", method = GET)
    @ResponseBody
    public Optional<User> getUserByID(@PathVariable Long user_id){
        return userService.getUserByID(user_id);
    }

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
}
