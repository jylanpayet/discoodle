package com.discoodle.api.controller;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.model.User;
import com.discoodle.api.service.FriendshipsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController("api/friendships")
public class FriendshipsController {
    private final FriendshipsService friendshipService;

    @PostMapping(path = "inviteMembers/{user_id}")
    public String inviteMembers(@PathVariable Long user_id, @RequestBody Friendships.FriendshipsRequest friends_id) {
        return friendshipService.inviteMembers(user_id, friends_id.getFriendships());
    }

    @GetMapping(path = "getAllInvitations/{user_id}")
    public List<Friendships> getAllInvitations(@PathVariable Long user_id) {
        return friendshipService.getAllInvitations(user_id);
    }

    @PutMapping(path = "acceptInvitation/{friendships_id}")
    public void acceptInvitation(@PathVariable Long friendships_id) {
        friendshipService.acceptInvitation(friendships_id);
    }

    @DeleteMapping(path = "refuseInvitation/{friendships_id}")
    public void refuseInvitation(@PathVariable Long friendships_id) {
        friendshipService.refuseInvitation(friendships_id);
    }
}
