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
@Table(name = "group_rights")
public class GroupRights {

    public GroupRights(boolean canDeleteUser, boolean canAddUser, boolean canModify) {
        this.canDeleteUser = canDeleteUser;
        this.canAddUser = canAddUser;
        this.canModify=canModify;
    }

    @Id
    @GeneratedValue
    @Column(name = "rights_id", unique = true, nullable = false)
    private Long rightsId;

    @Column(name = "can_delete_user")
    private boolean canDeleteUser;

    @Column(name = "can_add_user")
    private boolean canAddUser;

    @Column(name = "can_modify")
    private boolean canModify;
}
