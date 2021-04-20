package com.discoodle.api.controller;

import com.discoodle.api.model.Friendships;
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

    @PutMapping(path = "acceptInvitation/")
    public void acceptInvitation(@RequestBody Friendships.FriendshipsRequest request) {
        friendshipService.acceptInvitation(request.getSender_id(), request.getReceiver_id());
    }

    @DeleteMapping(path = "refuseInvitation")
    public void refuseInvitation(@RequestBody Friendships.FriendshipsRequest request) {
        friendshipService.refuseInvitation(request.getSender_id(), request.getReceiver_id());
    }

}
