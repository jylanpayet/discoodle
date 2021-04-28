package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id", unique = true, nullable = false)
    private Long message_id;

    @Column(name = "conv_uuid")
    private String conv_uuid;

    @Column(name = "content")
    private String content;

    @Column(name = "sender")
    private String sender;

    @Column(name = "message_date")
    private Long message_date;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "edited")
    private boolean edited;

    @Column(name = "type")
    private String type;


    public Message(String content, String sender, Long message_date, String conv_uuid) {
        this.content = content;
        this.sender = sender;
        this.pinned = false;
        this.message_date = message_date;
        this.conv_uuid = conv_uuid;
        this.type = "MESSAGE";
        this.edited = false;
    }
}
