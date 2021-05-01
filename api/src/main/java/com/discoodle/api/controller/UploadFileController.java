package com.discoodle.api.controller;

import com.discoodle.api.service.UploadFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController("api/uploadfile")
public class UploadFileController {

    private final UploadFileService uploadFileService;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("group_id") Long group_id) {
        System.out.println(file.getOriginalFilename());
        return uploadFileService.uploadFile(file, group_id);
    }

    @PostMapping(value = "/uploadSubject")
    public String uploadSubject(@RequestParam("file") MultipartFile file, @RequestParam("group_id") Long group_id) {
        return uploadFileService.uploadSubject(file, group_id);
    }

    @PostMapping(value = "/uploadImageInChat")
    public String uploadImageInChat(@RequestParam("file") MultipartFile file, @RequestParam("room_id") String room_id) {
        return uploadFileService.uploadImageInChat(file, room_id);
    }

    @PostMapping(value = "/uploadAvatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("user_id") Long user_id) {
        return uploadFileService.uploadAvatar(file, user_id);
    }
    @DeleteMapping("/deleteAvatar")
    public Boolean deleteAvatar(@RequestParam("user_id") Long user_id){
        return uploadFileService.deleteAvatar(user_id);
    }

    @PostMapping(value = "/uploadRoomAvatar")
    public String uploadRoomAvatar(@RequestParam("file") MultipartFile file, @RequestParam("room_id") String room_id) {
        return uploadFileService.uploadRoomAvatar(file, room_id);
    }
    @DeleteMapping("/deleteRoomImage")
    public Boolean deleteRoomAvatar(@RequestParam("room_id") String room_id){
        return uploadFileService.deleteRoomAvatar(room_id);
    }

}
