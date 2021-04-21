package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "server")
public class Server {

    public Server(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    @Column(name = "server_id", unique = true, nullable = false)
    private Long server_id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToOne
    @JoinTable(name = "link_groups_to_server",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id"))
    private Groups group;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "link_server_room",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "link_server_users",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

}
