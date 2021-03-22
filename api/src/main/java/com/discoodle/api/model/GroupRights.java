package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "group_rights")
public class GroupRights {

    public GroupRights(boolean canDeleteUser, boolean canAddUser) {
        this.canDeleteUser = canDeleteUser;
        this.canAddUser = canAddUser;
    }

    @Id
    @GeneratedValue
    @Column(name = "rights_id", unique = true, nullable = false)
    private Integer RightsId;

    @Column(name = "can_delete_user")
    private boolean canDeleteUser;

    @Column(name = "can_add_user")
    private boolean canAddUser;



}
