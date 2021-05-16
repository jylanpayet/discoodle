package com.discoodle.api.controller;

import com.discoodle.api.model.TeacherRequest;
import com.discoodle.api.service.TeacherRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PutMapping("/acceptTeacherRequest")
    public Optional<TeacherRequest> acceptTeacherRequest(@RequestParam("user_id") Long user_id) {
        return teacherRequestService.acceptTeacherRequest(user_id);
    }

    @PutMapping("/refuseTeacherRequest")
    public Optional<TeacherRequest> refuseTeacherRequest(@RequestParam("user_id") Long user_id) {
        return teacherRequestService.refuseTeacherRequest(user_id);
    }

    @GetMapping("/getTeacherRequestOfUser")
    public Optional<TeacherRequest> getTeacherRequestOfUser(@RequestParam("user_id") Long user_id){
        return teacherRequestService.getTeacherRequestOfUser(user_id);
    }

    @GetMapping("/getTeacherRequestBeingProcessed")
    public List<TeacherRequest> getTeacherRequestBeingProcessed(){
        return teacherRequestService.getTeacherRequestBeingProcessed();
    }
}
