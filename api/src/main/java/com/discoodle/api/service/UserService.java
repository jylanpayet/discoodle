package com.discoodle.api.service;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.FriendshipsRepository;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final FriendshipsRepository friendshipsRepository;
    private final RoomService roomService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUserName(String username) {
        return userRepository.getUserByUserName(username);
    }

    public Optional<User> getUserByMail(String mail) {
        return userRepository.getUserByMail(mail);
    }

    public Optional<User> getUserByID(Long user_id) {
        return userRepository.findById(user_id);
    }

    public Optional<User.Role> getUserByRole(User.Role role) {
        return userRepository.getUserByRole(role);
    }

    public void addNewUser(User user) {
        Optional<User> TestPseudo = userRepository.getUserByUserName(user.getUsername());
        Optional<User> TestMail = userRepository.getUserByMail(user.getMail());

        if (TestPseudo.isPresent() || TestMail.isPresent()) {
            throw new IllegalStateException("Le pseudo est déjà pris.");
        }
        userRepository.save(user);
    }

    public boolean deleteUser(Long user_id) {
        Optional<User> delete = userRepository.findById(user_id);
        if (delete.isPresent()) {
            userRepository.removeToken(user_id);
            for (Room test_room : delete.get().getRooms()) {
                roomService.removeMember(test_room.getRoom_id(), user_id);
            }
            List<Long> list = userRepository.getFriendListForSender(user_id);
            list.addAll(userRepository.getFriendListForReceiver(user_id));
            List<Friendships> friendships = friendshipsRepository.getALlRelations(user_id);
            for (Friendships test_friendships : friendships) {
                friendshipsRepository.deleteById(test_friendships.getFriendships_id());
            }
            userRepository.deleteById(user_id);
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return (UserDetails) userRepository.getUserByMail(mail).orElseThrow(() ->
                new UsernameNotFoundException("L'utilisateur avec l'email " + mail + " n'a pas été trouvé."));
    }

    public String signUpUser(User user) {
        boolean userExist = userRepository.getUserByMail(user.getMail()).isPresent();

        if (userExist) {
            return "L'email est déjà utilisé.";
        }

        userExist = userRepository.getUserByUserName(user.getUsername()).isPresent();

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
        if (userRepository.getUserByUserName(username).isPresent()) {
            if (!bCryptPasswordEncoder.matches(password, userRepository.getUserByUserName(username).get().getPassword()))
                return "Mot de passe ou nom d'utilisateur incorrect";
            else
                return "";
        }
        return "Mot de passe ou nom d'utilisateur incorrect";
    }

    public int enableUser(String mail) {
        return userRepository.enableUser(mail);
    }

    public List<User> getFriendList(Long user_id) {
        if (!userRepository.existsById(user_id))
            return List.of();
        List<Long> list = userRepository.getFriendListForReceiver(user_id);
        list.addAll(userRepository.getFriendListForSender(user_id));
        return list.stream().filter(userRepository::existsById).map(elt -> userRepository.findById(elt).get()).collect(Collectors.toList());
    }

    public Optional<User> changeUsername(Long user_id, String username) {
        Optional<User> changeUsername = userRepository.findById(user_id);
        if (changeUsername.isPresent() && userRepository.changeUsername(user_id, username) == 1)
            return changeUsername;
        return Optional.empty();
    }

    public Optional<User> changeMail(Long user_id, String mail) {
        Optional<User> changeMail = userRepository.findById(user_id);
        if (changeMail.isPresent() && mail.matches("^(.+)@(.+)$") && userRepository.changeMail(user_id, mail) == 1)
            return changeMail;
        return Optional.empty();
    }

    public Optional<User> changePassword(Long user_id, String password) {
        Optional<User> changePassword = userRepository.findById(user_id);
        if (changePassword.isPresent() && password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")) {
            String passwordEncoded = bCryptPasswordEncoder.encode(password);
            if (userRepository.changePassword(user_id, passwordEncoded) == 1)
                return changePassword;
        }
        return Optional.empty();
    }

    public Optional<User> changeName(Long user_id, String name) {
        Optional<User> changeName = userRepository.findById(user_id);
        if (changeName.isPresent()) {
            userRepository.changeName(user_id, name);
            return changeName;
        }
        return Optional.empty();
    }

    public Optional<User> changeLastName(Long user_id, String last_name) {
        Optional<User> changeLastName = userRepository.findById(user_id);
        if (changeLastName.isPresent()) {
            userRepository.changeLastName(user_id, last_name);
            return changeLastName;
        }
        return Optional.empty();
    }

    public Optional<User> changeLinkToAvatar(Long user_id, String link_to_avatar) {
        Optional<User> changeLinkToAvatar = userRepository.findById(user_id);
        if (changeLinkToAvatar.isPresent()) {
            userRepository.changeLinkToAvatar(user_id, link_to_avatar);
            return changeLinkToAvatar;
        }
        return Optional.empty();
    }
}
