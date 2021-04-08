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

    public Room createNewRoom(String room_name, List<Long> room_members) {
        Room room = new Room(
                room_name,
                room_members.get(0),
                false
        );
        Room finalRoom = roomRepository.save(room);
        for (Long room_member : room_members) {
            roomRepository.addNewMember(finalRoom.getRoom_id(),room_member);
        }
        return finalRoom;
    }

    public void addNewMember(String room_id, Long user_id) {
        roomRepository.addNewMember(room_id, user_id);
    }

    public Optional<Room> addNewMembers(String room_id, List<Long> room_members) {
        for (Long room_member : room_members) {
            roomRepository.addNewMember(room_id,room_member);
        }
        return roomRepository.findRoomByUUID(room_id);
    }

    public Optional<Room> changeLinkPicture(String room_id, String link_to_avatar) {
        if(roomRepository.changeLinkPicture(room_id, link_to_avatar) == 1) {
            return roomRepository.findRoomByUUID(room_id);
        }
        return null;
    }

    public Optional<Room> changeAdmin(String room_id, Long room_admin) {
        if(roomRepository.changeAdmin(room_id, room_admin) == 1) {
            return roomRepository.findRoomByUUID(room_id);
        }
        return null;
    }
}
