package com.discoodle.api.repository;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user where user.username = ?1")
    Optional<User> findUserByUserName(String username);

    @Query("SELECT user FROM User user where user.id = ?1")
    Optional<User> findUserByID(Long user_ID);

    @Query("SELECT user FROM User user where user.mail = ?1")
    Optional<User> findUserByMail(String mail);

    @Query("SELECT friendships FROM Friendships friendships where friendships.receiver_id = ?1 AND friendships.status = false")
    List<Friendships> getFriendList(Long user_id);

    @Query("SELECT friendships.sender_id FROM Friendships friendships where friendships.receiver_id = ?1")
    List<Long> getFriendListForReceiver(Long user_id);

    @Query("SELECT friendships.receiver_id FROM Friendships friendships where friendships.sender_id = ?1")
    List<Long> getFriendListForSender(Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.enabled = true WHERE user.mail = ?1")
    int enableUser(String mail);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.username = :username WHERE user.id = :user_id")
    int changeUsername(@Param("user_id") Long user_id, @Param("username") String username);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.mail = :mail WHERE user.id = :user_id")
    int changeMail(@Param("user_id") Long user_id, @Param("mail") String mail);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.password = :password WHERE user.id = :user_id")
    int changePassword(@Param("user_id") Long user_id, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.name = :name WHERE user.id = :user_id")
    int changeName(@Param("user_id") Long user_id, @Param("name") String name);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.last_name = :last_name WHERE user.id = :user_id")
    int changeLastName(@Param("user_id") Long user_id, @Param("last_name") String last_name);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.link_to_avatar = :link_to_avatar WHERE user.id = :user_id")
    int changeLinkToAvar(@Param("user_id") Long user_id, @Param("link_to_avatar") String link_to_avatar);
}
