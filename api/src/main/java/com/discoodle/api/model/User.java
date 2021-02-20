package com.discoodle.api.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails {

    public User(String mail, String pseudo, String password, String name, String lastName, Date date, String linkToAvatar, UserRole role) {
        this.mail = mail;
        this.pseudo = pseudo;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.date = date;
        this.linkToAvatar = linkToAvatar;
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

    private String mail;

    private String pseudo;

    private String password;

    private String name;

    private String lastName;

    private Date date;

    private String linkToAvatar;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean locked = false;

    private Boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return pseudo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
