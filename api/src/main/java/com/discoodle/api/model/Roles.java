package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "roles")
public class Roles {

    public Roles(String name, String rights) {
        this.name = name;
        this.rights = rights;
    }

    @Id
    @GeneratedValue
    @Column(name = "role_id", unique = true, nullable = false)
    private Long role_id;

    @ManyToMany
    @JoinTable(name = "link_role_to_users",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "link_group_to_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "groups_id"))
    private Groups groups_id;


    @Column(name = "name")
    private String name;

    @Column(name = "rights")
    private String rights;

}
