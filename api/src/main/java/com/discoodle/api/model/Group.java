package com.discoodle.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class Group {

    Integer id;
    Group parent;
    String name;
    Integer depth;
    List<Group> underGroups;
    List<User> users;
    String usersGroupName;

    public Group(Group parent, String name, Integer depth, List<Group> underGroups, List<User> users, String usersGroupName) {
        this.parent = parent;
        this.name = name;
        this.depth = depth;
        this.underGroups = underGroups;
        this.users = users;
        this.usersGroupName = usersGroupName;
    }

}
