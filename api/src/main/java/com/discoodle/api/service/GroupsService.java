package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.*;
import com.discoodle.api.repository.RolesRepository;
import com.discoodle.api.repository.UserRepository;
import com.discoodle.api.request.*;
import com.discoodle.api.repository.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class GroupsService {

    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository;
    private final ServerService serverService;
    private final UserService userService;
    private final RolesRepository rolesRepository;
    private final NoteService noteService;

    public Optional<Groups> createNewGroup(GroupsRequest request) {
        // Generate an UUID token for this new Group.
        String token = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        // If the depth is 1, check if a group already has the same name in this depth.
        if(request.getType().equals(Groups.TypeOfGroup.ESTABLISHMENT) && groupsRepository.findAllGroupsByNameAndDepth(request.getName(), request.getDepth()).isPresent()){
            return Optional.empty();
        }
        // Create a new object Group.
        Groups group = new Groups(
                request.getParent_id(),
                request.getDepth(),
                request.getName(),
                request.getDescription(),
                request.getType(),
                token
        );
        // Save the object in the database with Spring.
        Groups finalGroup = groupsRepository.save(group);

        // Create two new unchangeable "Admin" and "Étudiant" roles.
        Roles role_admin = new Roles(
                "Admin",
                "*"
        );
        Roles role_student = new Roles(
                "Etudiant",
                "sr"
        );

        // Save roles in the database with Spring.
        Roles finalRole_admin = rolesRepository.save(role_admin);
        Roles finalRole_student = rolesRepository.save(role_student);

        // Create a link with Roles and Groups.
        groupsRepository.addRoleForGroup(finalGroup.getGroups_id(), finalRole_admin.getRole_id());
        groupsRepository.addRoleForGroup(finalGroup.getGroups_id(), finalRole_student.getRole_id());

        // Create a link with Roles "Admin" and the creator of this group.
        groupsRepository.addRoleForUser(request.getUser_id(), finalRole_admin.getRole_id());

        // Check if this new group have a parent group (Discoodle root has no parent).
        if (request.getParent_id() != null)
            // Create a link with the parent group and this new group.
            groupsRepository.addNewGroupsInGroup(request.getParent_id(), finalGroup.getGroups_id());

        // Add the creator in this new group.
        groupsRepository.addNewMemberInGroup(request.getUser_id(), finalGroup.getGroups_id());
        // Create a new server for this new group.
        Optional<Server> server = serverService.createNewServ("Serveur de " + finalGroup.getName(), List.of(request.getUser_id()));
        // Create link with this server and this new group.
        groupsRepository.addNewServInGroup(finalGroup.getGroups_id(), server.get().getServer_id());
        try {
            File dossier = new File((String.format("%sstatic/common/groups/%d", ApiApplication.RESSOURCES, finalGroup.getGroups_id())));
            // If the directory of this file does not exist, we create it.
            dossier.mkdirs();
        } catch (Exception e) {
            System.out.println("Dossier du groups non crée !");
        }
        // Return this new group.
        return groupsRepository.findById(finalGroup.getGroups_id());
    }

    public Optional<Groups> editGroup(EditGroupRequest request) {
        // Check if the group exist.
        if(groupsRepository.existsById(request.getGroups_id()))
            // If the group exist, we change its parameters.
            return groupsRepository.updateNameAndDescGroup(request.getGroups_id(), request.getName(), request.getDescription());
        return Optional.empty();
    }

    public void deleteGroupByID(Long groups_id) {
        // Check if the group exist.
        if (groupsRepository.existsById(groups_id)) {
            Optional<Groups> group = findGroupsByID(groups_id);
            // Delete the link with the parent and this group.
            groupsRepository.deleteLinkGroupsToGroup(group.get().getParent_id(), groups_id);
            // Delete this group and any links it might have.
            groupsRepository.deleteById(groups_id);
        }
    }

    public Optional<Groups> addNewMemberInGroup(Long groups_id, Long user_id, String token) {
        // Get the group and the user in database.
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        Optional<User> tempUser = userService.getUserByID(user_id);

        // Check if both are present, if the Token is good and if the user is not already present.
        if (tempGroup.isPresent() && tempUser.isPresent() && (tempGroup.get().getToken().equals(token)) && !tempGroup.get().getUsers().contains(tempUser.get())) {
            // Create link with the user and group.
            groupsRepository.addNewMemberInGroup(user_id, groups_id);

            // Give the "Étudiant" role for this user.
            groupsRepository.addRoleForUser(user_id, tempGroup.get().getRoles().stream().filter((e) -> e.getName().equals("Etudiant")).collect(Collectors.toList()).get(0).getRole_id());
            // Add the user in the server of group.
            serverService.addNewMember(tempGroup.get().getServer().getServer_id(), user_id);
            return tempGroup;
        }
        return Optional.empty();
    }

    public void removeMember(Long group_id, Long user_id) {
        groupsRepository.removeMember(group_id, user_id);
    }

    public Optional<Server> serverOfGroup(Long groups_id) {
        // Get the group in database.
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        // If group is present.
        if (tempGroup.isPresent())
            // Return his server.
            return Optional.of(tempGroup.get().getServer());
        return Optional.empty();
    }

    public Optional<Groups> findGroupsByID(Long groups_ID) {
        return groupsRepository.findGroupsByID(groups_ID);
    }

    public List<Groups> underGroup(Long groups_id) {
        // Get the group in database.
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        // If group is present.
        if (tempGroup.isPresent())
            // All the groups below him have returned.
            return tempGroup.get().getUnderGroups();
        return List.of();
    }

    public Optional<Roles> addRoleForGroup(Long group_id, String role_name, String rights) {
        // Check if group exist.
        if (groupsRepository.existsById(group_id)) {
            // Create new role with the parameters in the function.
            Roles role = new Roles(
                    role_name,
                    rights
            );
            // Save this role.
            rolesRepository.save(role);
            // Create a link with this group and this role.
            groupsRepository.addRoleForGroup(group_id, role.getRole_id());
            return Optional.of(role);
        }
        return Optional.empty();
    }

    public Optional<Roles> addRoleForUsers(List<Long> user_id, Long role_id) {
        // Check if this role exist.
        if (rolesRepository.findById(role_id).isPresent()) {
            // We add this role for all the users contained in the list.
            for (Long user : user_id) {
                if (!userRepository.findById(user).get().getRoles().contains(rolesRepository.findById(role_id).get())) {
                    groupsRepository.addRoleForUser(user, role_id);
                }
            }
            // Return this role.
            return rolesRepository.findById(role_id);
        }
        return Optional.empty();
    }

    public Optional<Roles> removeRoleForUser(Long user_id, Long role_id) {
        // Check if the role and the user are present.
        if (rolesRepository.findById(role_id).isPresent() && userRepository.findById(user_id).isPresent())
            // Remove user role.
            groupsRepository.removeRoleForUser(user_id, role_id);
        return Optional.empty();
    }

    public List<Roles> getRoleByGroupAndUser(Long group_id, Long user_id) {
        List<Roles> roles = userRepository.findById(user_id).get().getRoles();
        List<Roles> res = new java.util.ArrayList<>();
        // The variable "roles" is all the roles of this user and if a role from this list is linked to the group, we add it to the "res" list.
        for (Roles user : roles) {
            if (user.getGroups_id().getGroups_id().equals(group_id))
                res.add(user);
        }
        return res;
    }

    public Optional<Roles> modifyRightsForRole(Long role_id, String rights) {
        Optional<Roles> roles = rolesRepository.findById(role_id);
        // If the role is present.
        if (roles.isPresent()) {
            // Modify its parameters.
            groupsRepository.modifyRightsForRole(role_id, rights);
        }
        return roles;
    }

    public Boolean deleteRole(Long role_id) {

        Optional<Roles> role = rolesRepository.findById(role_id);
        // If the role exist.
        if (role.isPresent()) {

            // Remove all links between this role and its users.
            for (User user : role.get().getUsers()) {
                List<Roles> roles = this.getRoleByGroupAndUser(role.get().getGroups_id().getGroups_id(), user.getId());
                // If the user has no more roles, give him the student role by default.
                if (roles.size() == 1 && roles.get(0).getRole_id().equals(role_id)) {
                    groupsRepository.addRoleForUser(user.getId(), role.get().getGroups_id().getRoles().stream().filter(
                          e -> e.getName().equals("Etudiant")
                    ).collect(Collectors.toList()).get(0).getRole_id());
                }
            }
            // Delete this role.
            rolesRepository.deleteById(role_id);

            return true;
        }
        return false;
    }

    // Get the group of Discoodle, it is the root of all groups.
    public Optional<Long> findIDOfDiscoodle() {
        return groupsRepository.findIDOfDiscoodle();
    }


    public Boolean deleteUser(Long user_id, Long group_id) {
        // Check if the group and user exist.
        if (userRepository.existsById(user_id) && groupsRepository.existsById(group_id)) {
            List<Roles> roles=this.getRoleByGroupAndUser(group_id,user_id);
            // Remove all user's links with group roles.
            for(Roles r:roles){
                rolesRepository.deleteLinkRoleToUser(user_id,r.getRole_id());
            }
            // Remove the user from the server of this group.
            serverService.removeMember(groupsRepository.findById(group_id).get().getServer().getServer_id(),user_id);

            // Delete the user of this group.
            groupsRepository.deleteLinkGroupsToUser(group_id,user_id);
            return true;
        }
        return false;
    }
}
