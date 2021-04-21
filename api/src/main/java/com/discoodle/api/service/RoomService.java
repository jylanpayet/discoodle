package com.discoodle.api.service;

import com.discoodle.api.model.*;
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

    public Room createNewRoom(String room_name, List<Long> room_members) {
        Room room = new Room(
                room_name,
                room_members.get(0),
                false
        );
        Room finalRoom = roomRepository.save(room);
        for (Long room_member : room_members) {
            Optional<User> temp = userRepository.findById(room_member);
            if (temp.isPresent()) {
                roomRepository.addNewMember(finalRoom.getRoom_id(),room_member);
                finalRoom.getUsers().add(temp.get());
            }
        }
        return finalRoom;
    }

    public Optional<Room> deleteMember(String room_id, Long user_id) {
        Optional<Room> room = roomRepository.findRoomByUUID(room_id);
        roomRepository.deleteMember(room_id, user_id);
        return room;
    }

    public void addNewMember(String room_id, Long user_id) {
        roomRepository.addNewMember(room_id, user_id);
    }

    public Optional<Room> addNewMembers(String room_id, List<Long> room_members) {
        for (Long room_member : room_members) {
            roomRepository.addNewMember(room_id, room_member);
        }
        return roomRepository.findById(room_id);
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        if(roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }

    public Optional<Room> changeAdmin(String room_id, Long room_admin) {
        if(roomRepository.changeAdmin(room_id, room_admin) == 1) {
            return roomRepository.findById(room_id);
        }
        return Optional.empty();
    }

}
