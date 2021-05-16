package com.discoodle.api.service;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.MessagesRepository;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessagesRepository messagesRepository;

    public Optional<Room> createNewRoom(String room_name, List<Long> room_members) {
        // Create a new object Room.
        Room room = new Room(
                room_name,
                room_members.get(0),
                false
        );
        // Save the object in the database with Spring.
        Room finalRoom = roomRepository.save(room);
        // Create link of all members present in the list room_members with the room.
        for (Long room_member : room_members) {
            // Check if the user exists.
            if (userRepository.existsById(room_member)) {
                roomRepository.addNewMember(finalRoom.getRoom_id(), room_member);
            }
        }
        // Return the final room.
        return roomRepository.findById(finalRoom.getRoom_id());
    }

    // Return the list of users present in the room.
    public List<User> getUserOfRoom(String room_id) {
        return roomRepository.getUserOfRoom(room_id);
    }

    // Return the administrator of the room.
    public Optional<User> getAdminOfRoom(String room_id) {
        return userRepository.findById(roomRepository.getAdminOfRoom(room_id));
    }

    public Optional<User> removeMember(String room_id, Long user_id) {
        Optional<User> test = userRepository.findById(user_id);

        // Check if the room exits and if the user is present.
        if (roomRepository.existsById(room_id) && test.isPresent()) {
            Optional<Room> room = roomRepository.findById(room_id);
            // If the list of room users is greater than 1 and if this room is linked to the user.
            if (room.get().getUsers().size() > 1 && room.get().getUsers().contains(test.get())) {
                // Remove the link of the room and user.
                roomRepository.removeMember(room_id, user_id);

                // Appoint a new administrator for the room.
                if (user_id.equals(room.get().getRoom_admin())) {
                    if (room.get().getUsers().get(1).getId().equals(user_id))
                        this.changeAdmin(room_id, room.get().getUsers().get(0).getId());
                    else
                        this.changeAdmin(room_id, room.get().getUsers().get(1).getId());
                }
                // Delete all messages of this user.
                this.getMessagesOfUser(userRepository.findById(user_id).get().getUsername())
                      .forEach(elt -> messagesRepository.deleteById(elt.getMessage_id()));
            } else {
                // If the room has no more users, delete the room and all of its messages.
                for(Message msg : messagesRepository.getMessagesOfRoom(room_id)) {
                    messagesRepository.deleteById(msg.getMessage_id());
                }
                roomRepository.deleteById(room_id);
            }
            // Return the user.
            return userRepository.findById(user_id);
        }
        return Optional.empty();
    }

    // Return all messages of this user.
    public List<Message> getMessagesOfUser(String user_id) {
        return messagesRepository.getMessagesOfUser(user_id);
    }

    public Optional<Room> addNewMember(String room_id, Long user_id) {
        Optional<User> test = userRepository.findById(user_id);
        Optional<Room> room=roomRepository.findById(room_id);
        // Checks whether the objects were successfully retrieved and whether the user is not already present on the room.
        if (test.isPresent() && room.isPresent() && !room.get().getUsers().contains(test.get())) {
            // Create a link with this user and this room.
            roomRepository.addNewMember(room_id, user_id);
        }
        return Optional.empty();
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        Optional<Room> test = roomRepository.findById(room_id);
        // Check if the room exists and if its photo has been changed.
        if (test.isPresent() && roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            // Return this room.
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }

    public Optional<Room> changeAdmin(String room_id, Long room_admin) {
        Optional<Room> test = roomRepository.findById(room_id);
        // Check if the room exists and if the admin has been changed.
        if (test.isPresent() && roomRepository.changeAdmin(room_id, room_admin) == 1) {
            // Return this room.
            return test;
        }
        return Optional.empty();
    }

    public Optional<Room> renameRoom(String room_id, String new_name) {
        Optional<Room> test = roomRepository.findById(room_id);
        // Check if room exists.
        if(test.isPresent()) {
            // Rename the room and return this.
            roomRepository.renameRoom(room_id, new_name);
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }
}
