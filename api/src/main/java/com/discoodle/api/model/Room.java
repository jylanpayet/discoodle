package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "rooms")
public class Room {

    public Room(String room_name, Long room_admin, Boolean room_link) {
        this.room_name = room_name;
        this.room_admin = room_admin;
        this.room_link = room_link;
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "room_id", unique = true, nullable = false)
    private String room_id;

    @Column(name = "room_name")
    private String room_name;

    @Column(name = "room_admin")
    private Long room_admin;

    @Column(name = "link_picture")
    private String link_picture;

    @Column(name = "room_link")
    private Boolean room_link;

    @ManyToMany
    @JoinTable(name = "link_rooms_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "link_server_room",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "server_id"))
    private Server server;

}
