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
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.LinkedList;
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

    public Optional<User> getUserByID(Long user_id) {
        return userRepository.findUserByID(user_id);
    }

    public Optional<User.Role> findUserByRole(User.Role role){return userRepository.findUserByRole(role); }

    public void addNewUser(User user) {
        Optional<User> TestPseudo = userRepository.findUserByUserName(user.getUsername());
        Optional<User> TestMail = userRepository.findUserByMail(user.getMail());

        if (TestPseudo.isPresent() || TestMail.isPresent()) {
            throw new IllegalStateException("Le pseudo est déjà pris.");
        }
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
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
                return "L'email est déjà utilisé.";
            }

            userExist = userRepository.findUserByUserName(user.getUsername()).isPresent();

            if (userExist) {
                return "Le nom d'utilisateur est déjà utilisé.";
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

    public String login(String username, String password) {
        if(userRepository.findUserByUserName(username).isPresent() && userRepository.findUserByUserName(username).get().isEnabled()) {
            if (!bCryptPasswordEncoder.matches(password, userRepository.findUserByUserName(username).get().getPassword()))
                return "Mot de passe ou nom d'utilisateur incorrect";
            else if(!userRepository.findUserByUserName(username).get().isEnabled())
                return "Veuillez valider votre compte par mail.";
            else
                return "";
        }
        return "Mot de passe ou nom d'utilisateur incorrect";
    }

    public int enableUser(String mail) {
        return userRepository.enableUser(mail);
    }

    public List<User> getFriendList(Long user_id) {
        List<Long> list = userRepository.getFriendListForReceiver(user_id);
        list.addAll(userRepository.getFriendListForSender(user_id));
        List<User> res = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            Optional<User> tmp = userRepository.findUserByID(list.get(i));
            tmp.ifPresent(res::add);
        }
        return res;
    }

    public Optional<User> changeUsername(Long user_id, String username) {
        if(!userRepository.findUserByUserName(username).isPresent() && userRepository.changeUsername(user_id, username) == 1) {
            return userRepository.findUserByID(user_id);
        }
        return null;
    }

    public Optional<User> changeMail(Long user_id, String mail) {
        if(mail.matches("^(.+)@(.+)$") && !userRepository.findUserByMail(mail).isPresent() && userRepository.changeMail(user_id, mail) == 1) {
         return userRepository.findUserByID(user_id);
        }
        return null;
    }

    public Optional<User> changePassword(Long user_id, String password) {
        if(password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")) {
            String passwordEncoded = bCryptPasswordEncoder.encode(password);
            if(userRepository.changePassword(user_id, passwordEncoded) == 1) {
                return userRepository.findUserByID(user_id);
            }
        }
        return null;
    }

    public Optional<User> changeName(Long user_id, String name) {
        if(userRepository.changeName(user_id, name) == 1) {
            return userRepository.findUserByID(user_id);
        }
        return null;
    }

    public Optional<User> changeLastName(Long user_id, String last_name) {
            if(userRepository.changeLastName(user_id, last_name) == 1) {
                return userRepository.findUserByID(user_id);
            }
        return null;
    }

    public Optional<User> addFriends(Long user_id, String link_to_avatar) {
        if(userRepository.changeLinkToAvar(user_id, link_to_avatar) == 1) {
            return userRepository.findUserByID(user_id);
        }
        return null;
    }

    public Optional<User> changeLinkToAvatar(Long user_id, String link_to_avatar) {
        if(userRepository.changeLinkToAvar(user_id, link_to_avatar) == 1) {
            return userRepository.findUserByID(user_id);
        }
        return null;
    }
}
