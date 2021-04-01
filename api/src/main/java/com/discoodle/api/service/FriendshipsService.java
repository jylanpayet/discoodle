package com.discoodle.api.service;

import com.discoodle.api.model.Friendships;

import com.discoodle.api.repository.FriendshipsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipsService {
    private final FriendshipsRepository friendshipsRepository;

    public List<Friendships> getAllInvitations(Long user_id) {
        return friendshipsRepository.getAllInvitations(user_id);
    }

    public void acceptInvitation(Long friendships_id) {
        friendshipsRepository.acceptInvitation(friendships_id);
    }

    public void refuseInvitation(Long friendships_id) {
        friendshipsRepository.deleteById(friendships_id);
    }

    public String inviteMembers(Long user_id, List<Long> friends_id) {
        for(int i = 0; i < friends_id.size(); i++) {
            Friendships friendships = new Friendships(
                    user_id,
                    friends_id.get(i)
            );
            friendshipsRepository.save(friendships);
        }
        return "";
    }
}
