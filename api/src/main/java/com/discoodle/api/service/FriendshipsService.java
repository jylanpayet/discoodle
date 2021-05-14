package com.discoodle.api.service;

import com.discoodle.api.model.Friendships;

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

    public String inviteMembers(Long user_id, List<Long> friends_id) {
        // Check if user exists.
        if(userRepository.existsById(user_id)) {
            // Pick up one by one all IDs from list friends_id (which embodies all members the user has invited).
            for (Long aLong : friends_id) {
                // Check if every members, specified with their ID in the list, exist.
                if (userRepository.existsById(aLong)) {
                    // Check if the relation doesn't already exist already :
                    // - the user has already sent an invitation to this member.
                    // - or the member specified in friends_id has already sent an invitation to user_id.
                    if (!userRepository.getFriendListCompleteForSender(user_id).contains(aLong) && !userRepository.getFriendListCompleteForReceiver(aLong).contains(user_id)) {
                        // Creation of the friendship.
                        Friendships friendships = new Friendships(
                                user_id,
                                aLong
                        );
                        // Save the friendship in database.
                        friendshipsRepository.save(friendships);
                        return "Votre invitation a été envoyé avec succès !";
                    }
                    return "Vous êtes déjà ami ou vous avez déjà une invitation avec cet utilisateur.";
                }
                return "L'utilisateur n'existe pas.";
            }
        }
        return "";
    }

    public List<Friendships> getAllInvitations(Long user_id) {
        // Check if user exists.
        if (userRepository.existsById(user_id)){
            // Bring back all friendships received by the user but not accepted.
            return friendshipsRepository.getAllInvitations(user_id);
        }
        return null;
    }

    public void acceptInvitation(Long sender_id, Long receiver_id) {
        // Check if users exist.
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.acceptInvitation(sender_id, receiver_id);
    }

    public void refuseInvitation(Long sender_id, Long receiver_id) {
        // Check if users exist.
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.refuseInvitation(sender_id, receiver_id);
    }

}
