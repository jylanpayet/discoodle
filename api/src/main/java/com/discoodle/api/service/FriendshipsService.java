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
        if(userRepository.existsById(user_id)) {
            for (Long aLong : friends_id) {
                if (userRepository.existsById(aLong)) {
                    if (!userRepository.getFriendListCompleteForSender(user_id).contains(aLong) && !userRepository.getFriendListCompleteForReceiver(aLong).contains(user_id)) {
                        System.out.println(userRepository.getFriendListForReceiver(aLong));
                        System.out.println(userRepository.getFriendListForSender(user_id));
                        Friendships friendships = new Friendships(
                                user_id,
                                aLong
                        );
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
        if (userRepository.existsById(user_id)){
            return friendshipsRepository.getAllInvitations(user_id);
        }
        return null;
    }

    public void acceptInvitation(Long sender_id, Long receiver_id) {
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.acceptInvitation(sender_id, receiver_id);
    }

    public void refuseInvitation(Long sender_id, Long receiver_id) {
        if(userRepository.existsById(sender_id) && userRepository.existsById(receiver_id))
            friendshipsRepository.refuseInvitation(sender_id, receiver_id);
    }

}
