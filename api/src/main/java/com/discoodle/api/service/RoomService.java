package com.discoodle.api.service;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.MessagesRepository;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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
            if (userRepository.existsById(room_member) && userRepository.existsById(room_member)) {
                roomRepository.addNewMember(finalRoom.getRoom_id(), room_member);
            }
        }
        return finalRoom;
    }

    public List<User> getUserOfRoom(String room_id) {
        return roomRepository.getUserOfRoom(room_id);
    }

    public Optional<User> getAdminOfRoom(String room_id) {
        return userRepository.findById(roomRepository.getAdminOfRoom(room_id));
    }

    public Optional<User> removeMember(String room_id, Long user_id) {
        Optional<User> test = userRepository.findById(user_id);
        if (roomRepository.existsById(room_id) && test.isPresent()) {
            Optional<Room> room = roomRepository.findById(room_id);
            if (room.get().getUsers().size() > 1 && room.get().getUsers().contains(test.get())) {
                roomRepository.removeMember(room_id, user_id);
                if (user_id.equals(room.get().getRoom_admin())) {
                    if (room.get().getUsers().get(1).getId().equals(user_id))
                        this.changeAdmin(room_id, room.get().getUsers().get(0).getId());
                    else
                        this.changeAdmin(room_id, room.get().getUsers().get(1).getId());
                }
                this.getMessagesOfUser(userRepository.findById(user_id).get().getUsername())
                      .forEach(elt -> messagesRepository.deleteById(elt.getMessage_id()));
            } else {
                for(Message msg : messagesRepository.getMessagesOfRoom(room_id)) {
                    messagesRepository.deleteById(msg.getMessage_id());
                }
                roomRepository.deleteById(room_id);
            }
            return userRepository.findById(user_id);
        }
        return Optional.empty();
    }

    public List<Message> getMessagesOfUser(String user_id) {
        return messagesRepository.getMessagesOfUser(user_id);
    }

    public Optional<Room> addNewMember(String room_id, Long user_id) {
        Optional<User> test = userRepository.findById(user_id);
        if (test.isPresent() && !roomRepository.findById(room_id).get().getUsers().contains(test.get())) {
            roomRepository.addNewMember(room_id, user_id);
        }
        return Optional.empty();
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        Optional<Room> test = roomRepository.findById(room_id);
        if (test.isPresent() && roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            return test;
        }
        return Optional.empty();
    }

    public Optional<Room> changeAdmin(String room_id, Long room_admin) {
        Optional<Room> test = roomRepository.findById(room_id);
        if (test.isPresent() && roomRepository.changeAdmin(room_id, room_admin) == 1) {
            return test;
        }
        return Optional.empty();
    }

    public Optional<Room> renameRoom(String room_id, String new_name) {
        Optional<Room> test = roomRepository.findById(room_id);
        if(test.isPresent()) {
            roomRepository.renameRoom(room_id, new_name);
        }
        return Optional.empty();
    }

}
