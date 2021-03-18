package com.discoodle.api.service;

import com.discoodle.api.model.User;
import com.discoodle.api.repository.UserRepository;
import com.discoodle.api.security.token.ConfirmationToken;
import com.discoodle.api.security.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUserName(String username) {
        return userRepository.findUserByUserName(username);
    }

    public Optional<User> getUserByID(Integer user_id) {
        return userRepository.findUserByID(user_id);
    }

    public void addNewUser(User user) {
        Optional<User> TestPseudo = userRepository.findUserByUserName(user.getUsername());
        Optional<User> TestMail = userRepository.findUserByMail(user.getMail());

        if (TestPseudo.isPresent() || TestMail.isPresent()) {
            throw new IllegalStateException("Le pseudo est déjà pris.");
        }
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("L'étudiant avec l'id : " + userId + "n'existe pas.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findUserByMail(mail).orElseThrow(() ->
                new UsernameNotFoundException("L'utilisateur avec l'email " + mail + " n'a pas été trouvé."));
    }

    public String signUpUser(User user) {
        boolean userExist = userRepository.findUserByMail(user.getMail()).isPresent();

        if (userExist) {
            throw new IllegalStateException("L'email est déjà utilisé.");
        }

        String passwordEncoded = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public Boolean login(String username, String password) {
        if(userRepository.findUserByUserName(username).isPresent() && userRepository.findUserByUserName(username).get().isEnabled()) {
            return bCryptPasswordEncoder.matches(password, userRepository.findUserByUserName(username).get().getPassword());
        }
        return false;
    }

    public int enableUser(String mail) {
        return userRepository.enableUser(mail);
    }

    public List<Integer> findAllRoomsByUserID(Integer user_id) {
        return findAllRoomsByUserID(user_id);
    }
}
