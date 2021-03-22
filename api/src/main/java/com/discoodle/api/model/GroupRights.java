package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GroupRights {

    private Integer id;
    private boolean canDeleteUser;
    private boolean canAddUser;

    public GroupRights(boolean canDeleteUser, boolean canAddUser) {
        this.canDeleteUser = canDeleteUser;
        this.canAddUser = canAddUser;
    }


}
