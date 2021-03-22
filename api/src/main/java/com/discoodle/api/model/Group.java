package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
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

    @Column(name = "parent")
    private Group parent;

    @Column(name = "name")
    private String name;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "under_groups")
    private List<Group> underGroups;

    @Column(name = "users")
    private List<User> users;

    @Column(name = "users_group_name")
    private String usersGroupName;

    @Column(name = "group_rights")
    private GroupRights groupRights;




}
