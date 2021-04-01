package com.discoodle.api.repository;

import com.discoodle.api.model.Friendships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface FriendshipsRepository extends JpaRepository<Friendships, Long> {
    @Query("SELECT friendships FROM Friendships friendships where friendships.receiver_id = ?1")
    List<Friendships> getAllInvitations(Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE Friendships friendships SET friendships.status = true WHERE friendships.friendships_id = ?1")
    void acceptInvitation(Long friendships_id);
}
