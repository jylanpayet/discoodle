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

    public User(String mail, String username, String password, String name, String last_name, UserRole role) {
        this.mail = mail;
        this.username = username;
        this.password = password;
        this.name = name;
        this.last_name = last_name;
        this.role = role;
    }

    @Id
    @GeneratedValue
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
    private String last_name;

    @Column(name = "link_to_avatar")
    private String linkToAvatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean locked;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

    @ManyToMany
    @JoinTable( name = "link_user_subject",
            joinColumns= @JoinColumn( name = "user_id" ),
            inverseJoinColumns = @JoinColumn( name = "subject_id" )
    )
    private List<User> users = new ArrayList<>();
}
