package com.discoodle.api.model;
import java.util.UUID;

public class ChatRoom {
    private String roomID;
    private String name;

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomID = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

    public String getName() {
        return name;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

}