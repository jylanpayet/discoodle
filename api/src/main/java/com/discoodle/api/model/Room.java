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
@Table(name = "room")
public class Room {

    public Room(String name, Integer user_ID) {
        this.name = name;
        this.user_ID = user_ID;
    }

    @Id
    /*@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")*/
    @GeneratedValue
    @Column(name = "room_id", unique = true, nullable = false)
    private Integer uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "admin")
    private Integer user_ID;
    @Column(name = "link_picture")
    private String link_picture;

    @JsonIgnore
    @ManyToMany
    @JoinTable( name = "link_rooms_users",
            joinColumns = @JoinColumn( name = "room_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<User> users = new ArrayList<>();
}
