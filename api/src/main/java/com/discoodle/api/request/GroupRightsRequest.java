package com.discoodle.api.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GroupRightsRequest {
    private final Long groupId;
    private final boolean addUser;
    private final boolean deleteUser;
    private final boolean modify;
}
