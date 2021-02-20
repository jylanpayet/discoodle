package com.discoodle.api.service;

import com.discoodle.api.model.User;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(String pseudo) {
        return userRepository.findUserByPseudo(pseudo);
    }

    public void addNewUser(User user) {
        Optional<User> TestPseudo = userRepository.findUserByPseudo(user.getPseudo());
        Optional<User> TestMail = userRepository.findUserByPseudo(user.getMail());

        if(TestPseudo.isPresent() || TestMail.isPresent()) {
            throw new IllegalStateException("pseudo déjà pris");
        }
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new IllegalStateException("L'étudiant avec l'id : " + userId + "n'existe pas.");
        }
        userRepository.deleteById(userId);
    }
}
