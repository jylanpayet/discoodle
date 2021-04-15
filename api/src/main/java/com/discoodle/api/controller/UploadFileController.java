package com.discoodle.api.controller;

import com.discoodle.api.service.UploadFileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
public class UploadFileController {
    private final UploadFileService uploadFileService;

    @PostMapping(value= "api/upload/{group_id}")
    public String uploadFile(@RequestParam("file") MultipartFile file,@PathVariable("group_id") Long group_id) {
        System.out.println(file.getOriginalFilename());
        return uploadFileService.uploadFile(file,group_id);
    }

    @PostMapping(value= "api/uploadSubject/{group_id}")
    public String uploadSubject(@RequestParam("file") MultipartFile file,@PathVariable("group_id") Long group_id){
        return uploadFileService.uploadSubject(file,group_id);
    }
}
