package com.discoodle.api.repository;

import com.discoodle.api.model.Friendships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FriendshipsRepository extends JpaRepository<Friendships, Long> {

    @Query("SELECT friendships FROM Friendships friendships where friendships.receiver_id = ?1 AND friendships.status = false")
    List<Friendships> getAllInvitations(Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE Friendships friendships SET friendships.status = true WHERE friendships.sender_id = :sender_id AND friendships.receiver_id = :receiver_id")
    void acceptInvitation(@Param("sender_id") Long sender_id, @Param("receiver_id") Long receiver_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friendships friendships WHERE friendships.sender_id = :sender_id AND friendships.receiver_id = :receiver_id")
    void refuseInvitation(@Param("sender_id") Long sender_id, @Param("receiver_id") Long receiver_id);

}
