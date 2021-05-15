package com.discoodle.api;

import com.discoodle.api.model.User;
import com.discoodle.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User test = userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        assertNotNull(test);
        assertEquals("Dylan", test.getName());
    }

    @Test
    public void testChangeUsername() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changeUsername(userRepository.getUserByMail("dylan_alexandre@outlook.fr").get().getId(), "TBHTest");
        assertEquals("TBHTest", userRepository.getUserByMail("dylan_alexandre@outlook.fr").get().getUsername());
    }

    @Test
    public void testChangeMail() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changeMail(userRepository.getUserByUserName("TBH").get().getId(), "test@outlook.fr");
        assertEquals("test@outlook.fr", userRepository.getUserByUserName("TBH").get().getMail());
    }

    @Test
    public void testChangePassword() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changePassword(userRepository.getUserByUserName("TBH").get().getId(), "Bastian");
        assertEquals("Bastian", userRepository.getUserByUserName("TBH").get().getPassword());
    }

    @Test
    public void testChangeName() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changeName(userRepository.getUserByUserName("TBH").get().getId(), "Jylan");
        assertEquals("Jylan", userRepository.getUserByUserName("TBH").get().getName());
    }

    @Test
    public void testChangeLastName() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changeLastName(userRepository.getUserByUserName("TBH").get().getId(), "CLIPAL");
        assertEquals("CLIPAL", userRepository.getUserByUserName("TBH").get().getLast_name());
    }

    @Test
    public void testChangeLinkAvatar() {
        userRepository.save(new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT));
        userRepository.changeLinkToAvatar(userRepository.getUserByUserName("TBH").get().getId(), "discoodle.fr/mon-image.png");
        assertEquals("discoodle.fr/mon-image.png", userRepository.getUserByUserName("TBH").get().getLink_to_avatar());
    }
}
