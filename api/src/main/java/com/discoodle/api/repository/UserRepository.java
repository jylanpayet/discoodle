package com.discoodle.api.repository;

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

    @Query("SELECT user FROM User user where user.username = :username")
    Optional<User> getUserByUserName(String username);

    @Query("SELECT user FROM User user where user.mail = :mail")
    Optional<User> getUserByMail(String mail);

    @Query("SELECT friendships.sender_id FROM Friendships friendships where friendships.receiver_id = :user_id")
    List<Long> getFriendListCompleteForReceiver(Long user_id);

    @Query("SELECT friendships.receiver_id FROM Friendships friendships where friendships.sender_id = :user_id")
    List<Long> getFriendListCompleteForSender(Long user_id);

    @Query("SELECT friendships.sender_id FROM Friendships friendships where friendships.receiver_id = :user_id AND friendships.status = true")
    List<Long> getFriendListForReceiver(Long user_id);

    @Query("SELECT friendships.receiver_id FROM Friendships friendships where friendships.sender_id = :user_id AND friendships.status = true")
    List<Long> getFriendListForSender(Long user_id);

    @Query("SELECT user FROM User user where user.role= :role")
    Optional<User.Role> getUserByRole(User.Role role);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.enabled = true WHERE user.mail = :mail")
    int enableUser(String mail);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.username = :username WHERE user.id = :user_id")
    int changeUsername(@Param("user_id") Long user_id, @Param("username") String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.mail = :mail WHERE user.id = :user_id")
    int changeMail(@Param("user_id") Long user_id, @Param("mail") String mail);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.password = :password WHERE user.id = :user_id")
    int changePassword(@Param("user_id") Long user_id, @Param("password") String password);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.name = :name WHERE user.id = :user_id")
    int changeName(@Param("user_id") Long user_id, @Param("name") String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.last_name = :last_name WHERE user.id = :user_id")
    int changeLastName(@Param("user_id") Long user_id, @Param("last_name") String last_name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User user SET user.link_to_avatar = :link_to_avatar WHERE user.id = :user_id")
    int changeLinkToAvatar(@Param("user_id") Long user_id, @Param("link_to_avatar") String link_to_avatar);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.role = :role WHERE user.id = :user_id")
    void changeRole(@Param("user_id") Long user_id, @Param("role") User.Role role);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.locked = :lock WHERE user.id = :user_id")
    void lockOrUnlockUser(@Param("user_id") Long user_id, @Param("lock") Boolean lock);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE ct FROM confirmation_token as ct where ct.user_id = :user_id", nativeQuery = true)
    void removeToken(@Param("user_id") Long user_id);

}
