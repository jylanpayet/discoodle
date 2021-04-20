package com.discoodle.api.controller;

import com.discoodle.api.model.User;
import com.discoodle.api.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody User.RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody User.RegistrationRequest request) {
        return registrationService.login(request);
    }

    @GetMapping( "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
