package com.discoodle.api.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class FriendshipsRequest {
    private final List<Long> friendships = new LinkedList<>();
}
