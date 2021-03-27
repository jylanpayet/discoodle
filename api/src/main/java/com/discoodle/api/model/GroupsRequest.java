package com.discoodle.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GroupsRequest {
    private final Integer parent_id;
    private final Integer user_id;
    private final Integer depth;
    private final String name;
    private final String description;
    private final Groups.TypeOfGroup type;
}
