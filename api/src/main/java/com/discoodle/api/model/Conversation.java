package com.discoodle.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@Setter
@ToString

public class Conversation {

    private LinkedList<Message> messages;

    public Conversation(LinkedList<Message> messages) {
        this.messages = messages;
    }

    public Conversation() {
        this(new LinkedList<>());
    }

}
