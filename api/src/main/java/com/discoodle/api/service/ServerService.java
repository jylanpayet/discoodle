package com.discoodle.api.service;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.Server;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.RoomRepository;
import com.discoodle.api.repository.ServerRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServerService {

    ServerRepository serverRepository;
    UserRepository userRepository;
    RoomService roomService;
    RoomRepository roomRepository;

    public Server createNewServ(String server_name, List<Long> server_members) {
        Server serv = new Server(
                server_name
        );
        Server finalServ = serverRepository.save(serv);
        for (Long server_member : server_members) {
            if(userRepository.existsById(server_member))
                serverRepository.addNewMember(finalServ.getServer_id(), server_member);
        }
        Room room = roomService.createNewRoom("Géneral", server_members);
        room.setRoom_link(true);
        serverRepository.addNewRoomInServ(finalServ.getServer_id(), room.getRoom_id());
        return finalServ;
    }

    public Optional<Server> addNewMember(Long server_id, Long user_id) {
        Optional<User> user=userRepository.findById(user_id);
        Optional<Server> server=serverRepository.findById(server_id);

        if(server.isPresent() && user.isPresent() && !server.get().getUsers().contains(user.get())){
            serverRepository.addNewMember(server_id, user_id);

            for (Room room : server.get().getRooms()) {
                roomService.addNewMember(room.getRoom_id(), user_id);
            }
            return serverRepository.findById(server_id);
        }
        return Optional.empty();
    }

    public Optional<Room> addNewRoom(Long server_id, String name) {
        if (serverRepository.existsById(server_id)) {
            Optional<Server> server = serverRepository.findById(server_id);
            List<Long> users_id = new LinkedList<>();
            for (User user : server.get().getUsers()) {
                users_id.add(user.getId());
            }
            Room newRoom = roomService.createNewRoom(name, users_id);
            newRoom.setRoom_link(true);
            serverRepository.addNewRoomInServ(server_id, newRoom.getRoom_id());
            return roomRepository.findById(newRoom.getRoom_id());
        }
        return Optional.empty();
    }

    public List<Room> findAllRoomsByServerID(Long server_id) {
        List<Room> all = serverRepository.findById(server_id).get().getRooms();
        List<Room> rooms = new ArrayList<>();
        for (Room room : all) {
            if (room.getRoom_link()) {
                rooms.add(room);
            }
        }
        return rooms;
    }
}
