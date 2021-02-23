package com.discoodle.api.model;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString

public class ChatRoom {
    private String roomID;
    private String name;

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomID = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}