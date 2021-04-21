package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "friendships")
public class Friendships {

    public Friendships(Long sender_id, Long receiver_id) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friendships_id", unique = true, nullable = false)
    private Long friendships_id;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean status = false;

    @Column(name = "sender_id")
    private Long sender_id;

    @Column(name = "receiver_id")
    private Long receiver_id;

    @Getter
    @EqualsAndHashCode
    @ToString
    public static class FriendshipsRequest {
        private final List<Long> friendships = new LinkedList<>();
    }
}
