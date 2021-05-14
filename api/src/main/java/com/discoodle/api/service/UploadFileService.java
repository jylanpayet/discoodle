package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.GroupsRepository;
import com.discoodle.api.repository.RoomRepository;
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
import java.util.UUID;

@Service
@AllArgsConstructor
public class UploadFileService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final GroupsRepository groupsRepository;
    private final RoomRepository roomRepository;

    // Skeleton of our upload method.
    public String uploadFile(MultipartFile file, Long group_id) {
        // Check if group exists.
        if(!groupsRepository.existsById(group_id))
            return "le group n'existe pas !";
        // Create a path with the name of file.
        String path = String.format("%sstatic/common/groups/%d/" + file.getOriginalFilename(), ApiApplication.RESSOURCES, group_id);
        File add = new File(path);
        try {
            int i = 1;
            // Check if the file exists in the tree structure.
            while (add.exists()){

                // Creates a new path with a counter for the supposedly nonexistent filename.
                String index = FilenameUtils.removeExtension(file.getOriginalFilename()) + "(" + i + ")." + FilenameUtils.getExtension(file.getOriginalFilename());
                path = String.format("%sstatic/common/groups/%d/" + index, ApiApplication.RESSOURCES, group_id);
                add = new File(path);
                i++;
            }
            // If the file does not exist we create it in the tree structure and we transfer the MultipartFile inside.
            add.createNewFile();
            Files.write(Path.of(path), file.getBytes());
        } catch (IOException e) {
            System.out.println(e);
        }
        return "Fichier upload avec succès !";
    }

    // Refer to the uploadFile function.
    public String uploadImageInChat(MultipartFile file, String room_id) {
        if (roomRepository.findById(room_id).isEmpty())
            return "La room n'existe pas.";
        String extension =  FilenameUtils.getExtension(file.getOriginalFilename());
        // Check if the extension is an accepted extension (jpg,png)
        if (extension != null && !extension.equals("jpg") && !extension.equals("png")) {
            return "L'extension n'est pas un fichier jpg ou png, il ne peut donc pas être upload";
        }
        String uuid = UUID.randomUUID().toString();
        String path = String.format("%sstatic/common/images/rooms/%s.%s", ApiApplication.RESSOURCES, uuid, extension);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            }
            Files.write(Path.of(path), file.getBytes());
        } catch (Exception e) {
            return "Erreur lors du téléchargement de l'image !";
        }
        // Return the upload image with the mapping url of this file.
        return "http://localhost:8080/common/images/rooms/" + uuid + "." + extension;
    }

    // Refer to the uploadFile function.
    public String uploadSubject(MultipartFile file, Long group_id) {
        Optional<Groups> group=groupsRepository.findGroupsByID(group_id);
        // Check if this groups is a Subjects.
        if(group.isEmpty() || !group.get().getType().equals(Groups.TypeOfGroup.SUBJECTS))
            return "le group n'existe pas ou n'est pas une matière!";
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // Check if the extension is an accepted extension (jpg,png)
        if (!extension.equals("md") && !extension.equals(".txt")) {
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

    // Refer to the uploadFile function.
    public String uploadAvatar(MultipartFile file, Long user_id) {
        // Check if user exists.
        if(!userRepository.existsById(user_id))
            return "l'user n'existe pas !";
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        // Check if the extension is an accepted extension (jpg,png)
        if(!extension.equals("jpg") && !extension.equals("png")){
            return "Une erreur est survenue lors du téléchargements !";
        }
        String path = String.format("%sstatic/common/avatar/%d.%s", ApiApplication.RESSOURCES, user_id,extension);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            }
            // Change the avatar of this user.
            userService.changeLinkToAvatar(user_id,String.format("http://localhost:8080/common/avatar/%d.%s", user_id, extension));
            Files.write(Path.of(path), file.getBytes());
        } catch (Exception e) {
            return "Erreur lors du téléchargement du fichier !";
        }
        // Return the mapping url of this image.
        return String.format("http://localhost:8080/common/avatar/%d.%s", user_id, extension);
    }

    public Boolean deleteAvatar(Long user_id){
        Optional<User> user=userRepository.findById(user_id);
        // Check if user exists.
        if(user.isPresent()) {
            String path = String.format("%sstatic/common/avatar/", ApiApplication.RESSOURCES) + user.get().getLink_to_avatar().substring(36);
            try {
                // Delete the image file in the path, if this file exists.
                Files.deleteIfExists(Path.of(path));
            } catch (Exception e) {
                return false;
            }
            user.get().setLink_to_avatar(null);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    // Refer to the uploadFile function.
    public String uploadRoomAvatar(MultipartFile file, String room_id) {
        // Check if room exists.
        if(!roomRepository.existsById(room_id))
            return "la room n'existe pas !";
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        // Check if the extension is an accepted extension (jpg,png)
        if(!extension.equals("jpg") && !extension.equals("png")){
            return "Une erreur est survenue lors du téléchargements !";
        }
        String path = String.format("%sstatic/common/roomAvatar/%d.%s", ApiApplication.RESSOURCES,room_id,extension);
        File add = new File(path);
        try {
            if (!add.exists()) {
                add.createNewFile();
            }
            // Change the picture of room avatar.
            roomService.changeLinkPicture(room_id,path);
            Files.write(Path.of(path), file.getBytes());
        } catch (Exception e) {
            return "Erreur lors du téléchargement du fichier !";
        }
        // Return the mapping url of this image.
        return String.format("%sstatic/common/roomAvatar/%d.%s", ApiApplication.RESSOURCES,room_id,extension);
    }

    public Boolean deleteRoomAvatar(String room_id){
        Optional<Room> room=roomRepository.findById(room_id);
        // Check if room exists.
        if(room.isPresent()) {
            String path = String.format("%sstatic/common/roomAvatar/", ApiApplication.RESSOURCES) + room.get().getLink_picture().substring(40);
            try {
                // Delete the image file in the path, if this file exists.
                Files.deleteIfExists(Path.of(path));
            } catch (Exception e) {
                return false;
            }
            room.get().setLink_picture(null);
            roomRepository.save(room.get());
            return true;
        }
        return false;
    }

}
