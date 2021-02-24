package com.discoodle.api.model;

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

    public User(String mail, String username, String password, String name, String lastName, UserRole role) {
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.role = role;
    }

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "id", unique = true, nullable = false)
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
    private String lastName;

    @Column(name = "link_to_avatar")
    private String linkToAvatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "locked")
    private Boolean locked = false;

    @Column(name = "enabled")
    private Boolean enabled = false;

    @ManyToMany
    @JoinTable( name = "link_user_subject",
            joinColumns= @JoinColumn( name = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "subject_id" )
    )
    private List<User> users = new ArrayList<>();
}
