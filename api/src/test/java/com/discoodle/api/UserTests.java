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
        User user = new User("dylan_alexandre@outlook.fr", "TBH", "test", "Dylan", "ALEXANDRE", User.Role.STUDENT);
        User test = userRepository.save(user);
        assertNotNull(test);
        assertEquals("Dylan", test.getName());
    }

    @Test
    public void testCreateUserWithBadParameters() {

    }

    @Test
    public void testChangeUsername() {

    }

    @Test
    public void testChangePassword() {

    }

    @Test
    public void testChangeLinkAvatar() {

    }
}
