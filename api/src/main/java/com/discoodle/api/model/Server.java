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
@Table(name = "server")
public class Server {

    public Server(String name){
        this.name=name;
    }

    @Id
    @GeneratedValue
    @Column(name = "server_id", unique = true, nullable = false)
    private Long server_id;

    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinTable( name = "link_server_room",
            joinColumns = @JoinColumn( name = "server_id" ),
            inverseJoinColumns = @JoinColumn( name = "room_id" ) )
    private List<Room> rooms = new ArrayList<>();

}
