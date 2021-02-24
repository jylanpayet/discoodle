package com.discoodle.api.model;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "ChatRoom")
public class ChatRoom {

    public ChatRoom(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(generator = "room_id")
    @GenericGenerator(name = "room_id", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "room_id",unique = true, nullable = false)
    private String roomID;
    @Column(name = "name")
    private String name;

}