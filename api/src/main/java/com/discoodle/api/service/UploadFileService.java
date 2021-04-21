package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadFileService {

    private UserService userService;
    private UserRepository userRepository;

    public String uploadFile(MultipartFile file, Long group_id) {
        String path = String.format("%sstatic/common/groups/%d/" + file.getOriginalFilename(), ApiApplication.RESSOURCES, group_id);
        File add = new File(path);
        try {
            int i = 1;
            while (add.exists()) {
                String index = FilenameUtils.removeExtension(file.getOriginalFilename()) + "(" + i + ")." + FilenameUtils.getExtension(file.getOriginalFilename());
                path = String.format("%sstatic/common/groups/%d/" + index, ApiApplication.RESSOURCES, group_id);
                add = new File(path);
                i++;
            }
            add.createNewFile();
            Files.write(Path.of(path), file.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }

        return "Fichier upload avec succès !";
    }

    public String uploadSubject(MultipartFile file, Long group_id) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equals("md")) {
            return "L'extension n'est pas un fichier md, il ne peut donc pas être upload";
        }
        String path = String.format("%sstatic/common/groups/%d/cours.md", ApiApplication.RESSOURCES, group_id);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            }
            Files.write(Path.of(path), file.getBytes());
        } catch (Exception e) {
            return "Erreur lors du téléchargement du fichier !";
        }
        return "Fichier upload avec succès !";
    }

    public String uploadAvatar(MultipartFile file, Long user_id) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(!extension.equals("jpg") && !extension.equals("png")){
            return "Une erreur est survenue lors du téléchargements !";
        }
        String path = String.format("%sstatic/common/avatar/%d.%s", ApiApplication.RESSOURCES, user_id,extension);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            }
            userService.changeLinkToAvatar(user_id,path);
            Files.write(Path.of(path), file.getBytes());
        } catch (Exception e) {
            return "Erreur lors du téléchargement du fichier !";
        }
        return "Fichier upload avec succès !";
    }

    public Boolean deleteImage(Long user_id) {
        Optional<User> user=userRepository.findUserByID(user_id);
        if(user.isPresent()){
            String path = String.format("%sstatic/common/avatar/", ApiApplication.RESSOURCES)+user.get().getLink_to_avatar().substring(29);
            File del=new File(path);
            if(del.exists()){
                del.delete();
            }
            user.get().setLink_to_avatar(null);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }
}
