package com.discoodle.api.repository;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user where user.username = ?1")
    Optional<User> findUserByUserName(String username);

    @Query("SELECT user FROM User user where user.id = ?1")
    Optional<User> findUserByID(Integer user_ID);

    @Query("SELECT user FROM User user where user.mail = ?1")
    Optional<User> findUserByMail(String mail);

    @Query("SELECT friendships.sender_id FROM Friendships friendships where friendships.receiver_id = ?1 AND friendships.status = true")
    List<Long> getFriendListForReceiver(Long user_id);

    @Query("SELECT friendships.receiver_id FROM Friendships friendships where friendships.sender_id = ?1 AND friendships.status = true")
    List<Long> getFriendListForSender(Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.enabled = true WHERE user.mail = ?1")
    int enableUser(String mail);
}
