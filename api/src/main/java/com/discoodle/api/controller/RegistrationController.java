package com.discoodle.api.controller;

import com.discoodle.api.request.RegistrationRequest;
import com.discoodle.api.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody RegistrationRequest request) {
        return registrationService.login(request);
    }

    @GetMapping( "/confirm")
    public String confirm(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        return registrationService.confirmToken(token, response);
    }
}
