package com.discoodle.api.controller;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.service.FriendshipsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/friendships")
@AllArgsConstructor
public class FriendshipsController {

    private final FriendshipsService friendshipService;

    @PostMapping("/inviteMembers/{user_id}")
    public String inviteMembers(@PathVariable Long user_id, @RequestBody Friendships.FriendshipsRequest friends_id) {
        return friendshipService.inviteMembers(user_id, friends_id.getFriendships());
    }

    @GetMapping("/getAllInvitations/{user_id}")
    public List<Friendships> getAllInvitations(@PathVariable Long user_id) {
        return friendshipService.getAllInvitations(user_id);
    }

    @PutMapping(path = "/acceptInvitation/")
    public void acceptInvitation(@RequestParam("sender_id") Long sender_id, @RequestParam("receiver_id") Long receiver_id) {
        friendshipService.acceptInvitation(sender_id, receiver_id);
    }

    @DeleteMapping(path = "/refuseInvitation")
    public void refuseInvitation(@RequestParam("sender_id") Long sender_id, @RequestParam("receiver_id") Long receiver_id) {
        friendshipService.refuseInvitation(sender_id, receiver_id);
    }

}
