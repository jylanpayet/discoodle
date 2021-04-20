package com.discoodle.api.request;

import com.discoodle.api.model.Groups;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GroupsRequest {

    private final Long parent_id;

    private final Long user_id;

    private final Integer depth;

    private final String name;

    private final String description;

    private final Groups.TypeOfGroup type;

    private final String text;

    private final String role_name;

    private final String rights;

}
