package com.discoodle.api.controller;

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

    @RequestMapping(value = "{pseudo}", method = GET)
    @ResponseBody
    public Optional<User> getUser(@PathVariable String pseudo){
        return userService.getUser(pseudo);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userID}")
    public void deleteUser(@PathVariable("userID") Integer userId) {
        userService.deleteUser(userId);
    }
}
