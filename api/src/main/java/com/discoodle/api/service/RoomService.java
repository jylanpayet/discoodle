package com.discoodle.api.service;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.MessagesRepository;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessagesRepository messagesRepository;

    public Room createNewRoom(String room_name, List<Long> room_members) {
        Room room = new Room(
                room_name,
                room_members.get(0),
                false
        );
        Room finalRoom = roomRepository.save(room);
        for (Long room_member : room_members) {
            if (userRepository.existsById(room_member)) {
                roomRepository.addNewMember(finalRoom.getRoom_id(), room_member);
            }
        }
        return finalRoom;
    }

    public List<User> findUserOfRoom(String room_id) {
        return roomRepository.findUserOfRoom(room_id);
    }

    public Optional<User> findAdminOfRoom(String room_id) {
        return userRepository.findById(roomRepository.findAdminOfRoom(room_id));
    }

    public Optional<User> removeMember(String room_id, Long user_id) {
        if (roomRepository.existsById(room_id) && userRepository.existsById(user_id)) {
            Optional<Room> room = roomRepository.findById(room_id);
            if (room.get().getUsers().size() > 1) {
                roomRepository.removeMember(room_id, user_id);
                System.out.println(room.get().getUsers().get(0).getId().equals(user_id));
                this.changeAdmin(room_id, room.get().getUsers().get(0).getId());
                this.findMessagesOfUser(userRepository.findById(user_id).get().getUsername())
                      .forEach(elt -> messagesRepository.deleteById(elt.getMessage_id()));
            } else {
                roomRepository.deleteById(room_id);
            }
            return userRepository.findById(user_id);
        }
        return Optional.empty();
    }

    public List<Message> findMessagesOfUser(String user_id) {
        return messagesRepository.findMessagesOfUser(user_id);
    }

    public void addNewMember(String room_id, Long user_id) {
        Optional<User> test = userRepository.findById(user_id);
        if (test.isPresent() && !roomRepository.findById(room_id).get().getUsers().contains(test.get())) {
            roomRepository.addNewMember(room_id, user_id);
        }
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        if (roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }

    public Optional<Room> changeAdmin(String room_id, Long room_admin) {
        if (roomRepository.changeAdmin(room_id, room_admin) == 1) {
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }

    public void renameRoom(String room_id, String new_name) {
        roomRepository.renameRoom(room_id, new_name);
    }

}
