package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Group {

    private Integer id;
    private Group parent;
    private String name;
    private Integer depth;
    private List<Group> underGroups;
    private List<User> users;
    private String usersGroupName;
    private GroupRights groupRights;

    public Group(Group parent, String name, Integer depth, List<Group> underGroups, List<User> users, String usersGroupName, GroupRights groupRights) {
        this.parent = parent;
        this.name = name;
        this.depth = depth;
        this.underGroups = underGroups;
        this.users = users;
        this.usersGroupName = usersGroupName;
        this.groupRights = groupRights;
    }

}
