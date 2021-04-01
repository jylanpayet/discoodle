package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "user")
public class User {

    public User(String mail, String username, String password, String name, String last_name, Role role) {
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.name = name;
        this.last_name = last_name;
        this.role = role;
    }

    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "link_to_avatar")
    private String link_to_avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean locked;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @JsonIgnore
    @ManyToMany
    @JoinTable( name = "link_rooms_users",
            joinColumns = @JoinColumn( name = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "room_id" )
    )
    private List<Room> rooms = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable( name = "link_groups_to_user",
            joinColumns = @JoinColumn( name = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "groups_id" ) )
    private List<Groups> groups = new ArrayList<>();

    public enum Role {
        STUDENT,
        TEACHER,
        ADMIN
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class RegistrationRequest {

        private final String mail;

        private final String username;

        private final String password;

        private final String name;

        private final String last_name;

        private final String link_to_avatar;

        private final User.Role role;
    }
}
