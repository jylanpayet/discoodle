package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@AllArgsConstructor
public class UploadFileService {


    public String uploadFile(MultipartFile file, Long group_id) {
        String path = String.format("%sstatic/common/groups/%d/" + file.getOriginalFilename(), ApiApplication.RESSOURCES, group_id);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            } else {
                //Ajouter des fichiers avec le même nom sans pouvoir les confondre => mettre des index dans les fichiers au même nom.
            }
            Files.write(Path.of(path), file.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
        return "Fichier upload avec succès !";
    }
}
