package com.discoodle.api.service;

import com.discoodle.api.model.Friendships;

import com.discoodle.api.model.Room;
import com.discoodle.api.repository.FriendshipsRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FriendshipsService {
    private final FriendshipsRepository friendshipsRepository;
    private final UserRepository userRepository;

    public List<Friendships> getAllInvitations(Long user_id) {
        return friendshipsRepository.getAllInvitations(user_id);
    }

    public void acceptInvitation(Long sender_id, Long receiver_id) {
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.acceptInvitation(sender_id, receiver_id);
    }

    public void refuseInvitation(Long sender_id, Long receiver_id) {
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.refuseInvitation(sender_id, receiver_id);
    }

    public String inviteMembers(Long user_id, List<Long> friends_id) {
        if(userRepository.existsById(user_id)) {
            for (int i = 0; i < friends_id.size(); i++) {
                if(userRepository.existsById(friends_id.get(i))) {
                    Friendships friendships = new Friendships(
                            user_id,
                            friends_id.get(i)
                    );
                    friendshipsRepository.save(friendships);
                }
            }
        }
        return "";
    }
}
