package com.discoodle.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GroupRightsRequest {
    private final Integer groupId;
    private final boolean addUser;
    private final boolean deleteUser;
    private final boolean modify;
}
