package com.discoodle.api.controller;

import com.discoodle.api.service.UploadFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class UploadFileController {

    private final UploadFileService uploadFileService;

    //devient pour upload pour room
    @PostMapping(value = "api/upload/{group_id}")
    public String uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("group_id") Long group_id) {
        System.out.println(file.getOriginalFilename());
        return uploadFileService.uploadFile(file, group_id);
    }
    // delete pour room

    @PostMapping(value = "api/uploadSubject/{group_id}")
    public String uploadSubject(@RequestParam("file") MultipartFile file, @PathVariable("group_id") Long group_id) {
        return uploadFileService.uploadSubject(file, group_id);
    }

    @PostMapping(value = "api/uploadAvatar/{user_id}")
    public String uploadAvatar(@RequestParam("file") MultipartFile file, @PathVariable("user_id") Long user_id) {
        return uploadFileService.uploadAvatar(file, user_id);
    }
    @DeleteMapping("api/deleteImage/{user_id}")
    public Boolean deleteAvatar(@PathVariable("user_id") Long user_id){
        return uploadFileService.deleteAvatar(user_id);
    }

    @PostMapping(value = "api/uploadRoomAvatar/{room_id}")
    public String uploadRoomAvatar(@RequestParam("file") MultipartFile file, @PathVariable("room_id") String room_id) {
        return uploadFileService.uploadRoomAvatar(file, room_id);
    }
    @DeleteMapping("api/deleteRoomImage/{room_id}")
    public Boolean deleteRoomAvatar(@PathVariable("room_id") String room_id){
        return uploadFileService.deleteRoomAvatar(room_id);
    }

}
