package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "User")
public class User {

    public User(String mail, String pseudo, String password, String name, String last_name, Date date, String link_to_avatar, UserRole role) {
        this.mail = mail;
        this.pseudo = pseudo;
        this.password = password;
        this.name = name;
        this.last_name = last_name;
        this.birth_day = date;
        this.link_to_avatar = link_to_avatar;
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

    @Column(name="mail")
    private String mail;

    @Column(name="pseudo")
    private String pseudo;

    @Column(name="password")
    private String password;

    @Column(name="name")
    private String name;

    @Column(name="last_name")
    private String last_name;

    @Column(name="birth_day")
    private Date birth_day;

    @Column(name="link_to_avatar")
    private String link_to_avatar;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name="locked")
    private Boolean locked = false;

    @Column(name="enabled")
    private Boolean enabled = false;
}
