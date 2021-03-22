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
@Table(name = "group")
public class Group {

    public Group(Group parent, String name, Integer depth, List<Group> underGroups, List<User> users, String usersGroupName, GroupRights groupRights) {
        this.parent = parent;
        this.name = name;
        this.depth = depth;
        this.underGroups = underGroups;
        this.users = users;
        this.usersGroupName = usersGroupName;
        this.groupRights = groupRights;
    }

    @Id
    @GeneratedValue
    @Column(name = "group_id", unique = true, nullable = false)
    private Integer group_id;

    @OneToOne
    @JoinColumn( name="parent")
    private Group parent;

    @Column(name = "name")
    private String name;

    @Column(name = "depth")
    private Integer depth;

    @OneToMany
    @JoinTable( name = "link_group_to_group",
            joinColumns = @JoinColumn( name = "group_id" ),
            inverseJoinColumns = @JoinColumn( name = "group_id" ) )
    private List<Group> underGroups = new ArrayList<>();

    @OneToMany
    @JoinTable( name = "link_group_to_user",
            joinColumns = @JoinColumn( name = "group_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<User> users = new ArrayList<>();

    @Column(name = "users_group_name")
    private String usersGroupName;

    @OneToOne
    @JoinColumn( name="group_rights" )
    private GroupRights groupRights;




}
