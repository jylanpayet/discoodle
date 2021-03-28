package com.discoodle.api.service;

import com.discoodle.api.model.*;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.request.RoomRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createNewRoom(RoomRequest request) {
        Room room = new Room(
                request.getName(),
                request.getAdmin()
        );
        Room finalRoom = roomRepository.save(room);
        roomRepository.addNewMember(request.getAdmin(), finalRoom.getUuid());
        return finalRoom;
    }
}
