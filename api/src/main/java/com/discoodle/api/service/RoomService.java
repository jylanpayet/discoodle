package com.discoodle.api.service;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createNewRoom(String room_name, List<Integer> room_members) {
        Room room = new Room(
                room_name,
                room_members.get(0)
        );
        Room finalRoom = roomRepository.save(room);
        for(int i = 0; i < room_members.size(); i++) {
            roomRepository.addNewMembers(room_members.get(i), finalRoom.getRoom_id());
        }
        return finalRoom;
    }

    public Optional<Room> addNewMembers(String room_id, List<Integer> room_members) {
        for(int i = 0; i < room_members.size(); i++) {
            roomRepository.addNewMembers(room_members.get(i), room_id);
        }
        return roomRepository.findRoomByUUID(room_id);
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        if(roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            return roomRepository.findRoomByUUID(room_id);
        }
        return null;
    }
}
