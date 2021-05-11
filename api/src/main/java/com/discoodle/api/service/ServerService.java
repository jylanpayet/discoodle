package com.discoodle.api.service;

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

    public Optional<Server> createNewServ(String server_name, List<Long> server_members) {
        // Create a new object Server.
        Server serv = new Server(
                server_name
        );
        // Save the object in the database with Spring.
        Server finalServ = serverRepository.save(serv);
        // If the user exists in the list, create a link with the server and this user.
        for (Long server_member : server_members) {
            if(userRepository.existsById(server_member))
                serverRepository.addNewMember(finalServ.getServer_id(), server_member);
        }
        // Create a new room for this server with the name "Géneral".
        Room room = roomService.createNewRoom("Géneral", server_members);
        // Indicates that the room is linked to a server and link this with the server.
        room.setRoom_link(true);
        serverRepository.addNewRoomInServ(finalServ.getServer_id(), room.getRoom_id());
        // Return the final server.
        return serverRepository.findById(finalServ.getServer_id());
    }

    public Optional<Server> addNewMember(Long server_id, Long user_id) {
        Optional<User> user=userRepository.findById(user_id);
        Optional<Server> server=serverRepository.findById(server_id);

        // Checks whether the objects were successfully retrieved and whether the user is not already present on the server.
        if(server.isPresent() && user.isPresent() && !server.get().getUsers().contains(user.get())){
            // Create a link with the user and this server.
            serverRepository.addNewMember(server_id, user_id);

            // Create a link with the user and all the rooms present in the server.
            for (Room room : server.get().getRooms()) {
                roomService.addNewMember(room.getRoom_id(), user_id);
            }
            // Return the final server.
            return serverRepository.findById(server_id);
        }
        return Optional.empty();
    }

    public Optional<Room> addNewRoom(Long server_id, String name) {
        // Check if the server exist.
        if (serverRepository.existsById(server_id)) {
            Optional<Server> server = serverRepository.findById(server_id);
            List<Long> users_id = new LinkedList<>();
            // Add all users present on the server in the list.
            for (User user : server.get().getUsers()) {
                users_id.add(user.getId());
            }
            // Create a new Room with the list of user.
            Room newRoom = roomService.createNewRoom(name, users_id);

            // Indicates that the room is linked to a server and link this with the server.
            newRoom.setRoom_link(true);
            serverRepository.addNewRoomInServ(server_id, newRoom.getRoom_id());
            // Return the final room.
            return roomRepository.findById(newRoom.getRoom_id());
        }
        return Optional.empty();
    }

    public List<Room> findAllRoomsByServerID(Long server_id) {
        List<Room> all = serverRepository.findById(server_id).get().getRooms();
        List<Room> rooms = new ArrayList<>();

        // If the list of rooms is not empty and these rooms are linked to a server.
        for (Room room : all) {
            if (room.getRoom_link()) {
                rooms.add(room);
            }
        }
        // Return theses rooms in a list.
        return rooms;
    }

    public Boolean removeMember(Long server_id, Long user_id) {

        // Check if the server and user exist.
        if (serverRepository.existsById(server_id) && userRepository.existsById(user_id)) {
            List<Room> all = serverRepository.findById(server_id).get().getRooms();
            // Remove the user in all rooms present in the server.
            for (Room room : all) {
                roomService.removeMember(room.getRoom_id(),user_id);
            }
            // Remove the user from the server.
            serverRepository.removeMember(server_id,user_id);
            return true;
        }
        return false;
    }
}
