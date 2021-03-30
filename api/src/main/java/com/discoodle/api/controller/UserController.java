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
    public void deleteUser(@PathVariable("user_id") Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping(path = "/seeAllRooms/{user_id}")
    @ResponseBody
    public List<Room> findAllRoomsByUserID(@PathVariable Long user_ID) {
        return userService.getUserByID(user_ID).get().getRooms();
    }

    @PostMapping(path = "changeMail/{user_id}")
    public Optional<User> changeMail(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changeMail(user_id, request.getMail());
    }

    @PostMapping(path = "changePassword/{user_id}")
    public Optional<User> changePassword(@PathVariable Long user_id, @RequestBody User.RegistrationRequest request) {
        return userService.changePassword(user_id, request.getPassword());
    }
}
