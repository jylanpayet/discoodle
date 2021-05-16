package com.discoodle.api.service;

import com.discoodle.api.model.Friendships;
import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.FriendshipsRepository;
import com.discoodle.api.repository.TeacherRequestRepository;
import com.discoodle.api.repository.UserRepository;
import com.discoodle.api.security.token.ConfirmationToken;
import com.discoodle.api.security.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
    private final TeacherRequestRepository teacherRequestRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUserName(String username) {
        // Find user according to the username refered in parameter.
        return userRepository.getUserByUserName(username);
    }

    public Optional<User> getUserByMail(String mail) {
        // Find user according to the username refered in parameter.
        return userRepository.getUserByMail(mail);
    }

    public Optional<User> getUserByID(Long user_id) {
        // Find user according to the ID refered in parameter.
        return userRepository.findById(user_id);
    }

    public Optional<User.Role> getUserByRole(User.Role role) {
        return userRepository.getUserByRole(role);
    }

    public void addNewUser(User user) {
        Optional<User> TestPseudo = userRepository.getUserByUserName(user.getUsername());
        Optional<User> TestMail = userRepository.getUserByMail(user.getMail());

        // Check if user or/and mail exist in database.
        if (TestPseudo.isPresent() || TestMail.isPresent()) {
            throw new IllegalStateException("Le pseudo est déjà pris.");
        }
        // If it doesn't exist, we add the user in database.
        userRepository.save(user);
    }

    public boolean deleteUser(Long user_id) {
        Optional<User> delete = userRepository.findById(user_id);
        // Check if user exists in database.
        if (delete.isPresent()) {
            // Remove every elements belong to user : token, all rooms which he is in, all friendships where he is associated and finally remove user from database.
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
        // Check if the mail refered in parameters exists already in database.
        if (userExist) {
            return "L'email est déjà utilisé.";
        }

        userExist = userRepository.getUserByUserName(user.getUsername()).isPresent();

        // Check if the username refered in parameters exists already in database.
        if (userExist) {
            return "Le nom d'utilisateur est déjà utilisé.";
        }

        // Encrypt password with BCrypt (a hash function).
        String passwordEncoded = bCryptPasswordEncoder.encode(user.getPassword());
        // Save password encrypted in user's details.
        user.setPassword(passwordEncoded);

        // Save user in database.
        userRepository.save(user);

        // Generate a token for mail verification required during registration process and information of creation in this token and tme allowed for the user to validate his registration.
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        // Save token and his details in database.
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public String login(String username, String password) {
        Optional<User> test = userRepository.getUserByUserName(username);
        // Check if user exists.
        if (userRepository.getUserByUserName(username).isPresent()) {
            // Check if password refered in the input during the login process matches with the password saved in database during the registration of the user.
            if (!bCryptPasswordEncoder.matches(password, userRepository.getUserByUserName(username).get().getPassword()))
                return "Mot de passe ou nom d'utilisateur incorrect";
            if(test.get().isEnabled())
                return "";
            else
                return "Vous n'avez pas encore activé votre compte";
        }
        return "Mot de passe ou nom d'utilisateur incorrect";
    }

    public int enableUser(String mail) {
        // Enable the account after mail verification if everything matches well.
        return userRepository.enableUser(mail);
    }

    public List<User> getFriendList(Long user_id) {
        // Check user doesn't exist and return an empty list.
        if (!userRepository.existsById(user_id))
            return List.of();
        // In case user exists, we take all friendships where the ID of the user refered in parameters is linked.
        List<Long> list = userRepository.getFriendListForReceiver(user_id);
        list.addAll(userRepository.getFriendListForSender(user_id));
        return list.stream().filter(userRepository::existsById).map(elt -> userRepository.findById(elt).get()).collect(Collectors.toList());
    }

    public Optional<User> changeUsername(Long user_id, String username) {
        Optional<User> changeUsername = userRepository.findById(user_id);
        // Check if user exists and the request to change username worked fine.
        if (changeUsername.isPresent() && userRepository.changeUsername(user_id, username) == 1)
            return changeUsername;
        return Optional.empty();
    }

    public Optional<User> changeMail(Long user_id, String mail) {
        Optional<User> changeMail = userRepository.findById(user_id);
        // Check if user exists and the request to change mail processed well.
        if (changeMail.isPresent() && mail.matches("^(.+)@(.+)$") && userRepository.changeMail(user_id, mail) == 1)
            return changeMail;
        return Optional.empty();
    }

    public Optional<User> changePassword(Long user_id, String password) {
        Optional<User> changePassword = userRepository.findById(user_id);
        // Check if user exists and the password refered in parameters matches well with the regex which was established.
        if (changePassword.isPresent() && password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")) {
            // Encrypt password with BCrypt.
            String passwordEncoded = bCryptPasswordEncoder.encode(password);
            // Check if the request to change password worked fine.
            if (userRepository.changePassword(user_id, passwordEncoded) == 1)
                return changePassword;
        }
        return Optional.empty();
    }

    public Optional<User> changeName(Long user_id, String name) {
        Optional<User> changeName = userRepository.findById(user_id);
        // Check if user exists.
        if (changeName.isPresent()) {
            userRepository.changeName(user_id, name);
            return changeName;
        }
        return Optional.empty();
    }

    public Optional<User> changeLastName(Long user_id, String last_name) {
        Optional<User> changeLastName = userRepository.findById(user_id);
        // Check if user exists.
        if (changeLastName.isPresent()) {
            userRepository.changeLastName(user_id, last_name);
            return changeLastName;
        }
        return Optional.empty();
    }

    public Optional<User> changeLinkToAvatar(Long user_id, String link_to_avatar) {
        Optional<User> changeLinkToAvatar = userRepository.findById(user_id);
        // Check if user exists.
        if (changeLinkToAvatar.isPresent()) {
            userRepository.changeLinkToAvatar(user_id, link_to_avatar);
            return changeLinkToAvatar;
        }
        return Optional.empty();
    }

    public String changeRole(Long user_id, User.Role role) {
        if (userRepository.findById(user_id).isPresent()) {
            if (role.equals(User.Role.STUDENT) || role.equals(User.Role.TEACHER) || role.equals(User.Role.ADMIN )) {
                if (role.equals(User.Role.STUDENT) && teacherRequestRepository.getTeacherRequestByUser(user_id).isPresent())
                    teacherRequestRepository.deleteById(teacherRequestRepository.getTeacherRequestByUser(user_id).get().getTr_id());
                userRepository.changeRole(user_id, role);
                return "Rôle modifié";
            }
            return "ERR|Rôle introuvable";
        }
        return "ERR|Utilisateur introuvable";
    }

    public Optional<User> lockOrUnlockUser(Long user_id, Boolean lock) {
        Optional<User> temp = userRepository.findById(user_id);
        if (temp.isPresent()) {
            userRepository.lockOrUnlockUser(user_id, lock);
            return temp;
        }
        return Optional.empty();
    }
}
