package com.discoodle.api.model;

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
@Table(name = "groups")
public class Groups {

    public Groups(String name, Integer depth, String usersGroupName, GroupRights groupRights) {
        this.name = name;
        this.depth = depth;
        this.usersGroupName = usersGroupName;
        this.groupRights = groupRights;
    }

    @Id
    @GeneratedValue
    @Column(name = "groups_id", unique = true, nullable = false)
    private Integer groups_id;

    @Column(name = "name")
    private String name;

    @Column(name = "depth")
    private Integer depth;

    @OneToMany
    @JoinTable( name = "link_groups_to_group",
            joinColumns = @JoinColumn( name = "groups_id"),
            inverseJoinColumns = @JoinColumn( name = "son_id", referencedColumnName = "groups_id"))
    private List<Groups> underGroups = new ArrayList<>();

    @ManyToMany
    @JoinTable( name = "link_groups_to_user",
            joinColumns = @JoinColumn( name = "groups_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<User> users = new ArrayList<>();

    @Column(name = "users_group_name")
    private String usersGroupName;

    @OneToOne
    @JoinColumn( name="group_rights")
    private GroupRights groupRights;




}
