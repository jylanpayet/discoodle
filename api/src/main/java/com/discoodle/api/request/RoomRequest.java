package com.discoodle.api.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RoomRequest {

    private final String room_name;

    private final String link_picture;

    private final List<Long> room_members;

    private final Long room_admin;

}
