package com.discoodle.api.controller;

import com.discoodle.api.model.TeacherRequest;
import com.discoodle.api.service.TeacherRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/TeacherRequest")
@AllArgsConstructor
public class TeacherRequestController {

    private final TeacherRequestService teacherRequestService;

    @PostMapping("/addTeacherRequest")
    public Optional<TeacherRequest> addNewTeacherRequest(@RequestParam("user_id") Long user_id) {
        return teacherRequestService.addNewTeacherRequest(user_id);
    }

    @PostMapping("/AcceptTeacherRequest")
    public Optional<TeacherRequest> acceptTeacherRequest(@RequestParam("user_id") Long user_id) {
        return teacherRequestService.acceptTeacherRequest(user_id);
    }

    @PostMapping("/RefuseTeacherRequest")
    public Optional<TeacherRequest> refuseTeacherRequest(@RequestParam("user_id") Long user_id) {
        return teacherRequestService.refuseTeacherRequest(user_id);
    }
}
