package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.*;
import com.discoodle.api.repository.RolesRepository;
import com.discoodle.api.repository.UserRepository;
import com.discoodle.api.request.*;
import com.discoodle.api.repository.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class GroupsService {

    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository;
    private final ServerService serverService;
    private final UserService userService;
    private final RolesRepository rolesRepository;

    public Optional<Groups> createNewGroup(GroupsRequest request) {
        String token = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        Groups group = new Groups(
                request.getParent_id(),
                request.getDepth(),
                request.getName(),
                request.getDescription(),
                request.getType(),
                token
        );
        Groups finalGroup = groupsRepository.save(group);
        Roles role_admin = new Roles(
                "Admin",
                "*"
        );
        Roles role_student = new Roles(
                "Etudiant",
                "-"
        );
        Roles finalRole_admin = rolesRepository.save(role_admin);
        Roles finalRole_student = rolesRepository.save(role_student);
        groupsRepository.addRoleForGroup(finalGroup.getGroups_id(), finalRole_admin.getRole_id());
        groupsRepository.addRoleForGroup(finalGroup.getGroups_id(), finalRole_student.getRole_id());
        groupsRepository.addRoleForUser(request.getUser_id(), finalRole_admin.getRole_id());
        if (request.getParent_id() != null)
            groupsRepository.addNewGroupsInGroup(request.getParent_id(), finalGroup.getGroups_id());
        groupsRepository.addNewMemberInGroup(request.getUser_id(), finalGroup.getGroups_id());

        if (finalGroup.getType().equals(Groups.TypeOfGroup.SUBJECTS)) {
            Server server = serverService.createNewServ("Serveur de " + finalGroup.getName(), List.of(request.getUser_id()));
            groupsRepository.addNewServInGroup(finalGroup.getGroups_id(), server.getServer_id());
            try {
                File dossier = new File((String.format("%sstatic/common/groups/%d", ApiApplication.RESSOURCES, finalGroup.getGroups_id())));
                dossier.mkdirs();
            } catch (Exception e) {
                System.out.println("Dossier du groups non crée !");
            }
        }
        return Optional.of(finalGroup);
    }

    public Optional<Groups> editGroup(EditGroupRequest request) {
        Optional<Groups> groups = groupsRepository.updateNameAndDescGroup(request.getGroups_id(), request.getName(), request.getDescription());
        return groupsRepository.findGroupsByID(groups.get().getGroups_id());
    }

    public void deleteGroupByID(Long groups_id) {
        if (groupsRepository.existsById(groups_id)) {
            Optional<Groups> group = findGroupsByID(groups_id);
            groupsRepository.deleteLinkGroupsToGroup(group.get().getParent_id(), groups_id);
            groupsRepository.deleteById(groups_id);
        }
    }

    public Optional<Groups> addNewMemberInGroup(Long groups_id, Long user_id, String token) {
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        Optional<User> tempUser = userService.getUserByID(user_id);
        if (tempGroup.isPresent() && tempUser.isPresent() && (tempGroup.get().getToken().equals(token)) && !tempGroup.get().getUsers().contains(tempUser.get())) {
            groupsRepository.addNewMemberInGroup(user_id, groups_id);
            serverService.addNewMember(tempGroup.get().getServer().getServer_id(), user_id);
            return tempGroup;
        }
        return Optional.empty();
    }

    public Optional<Server> serverOfGroup(Long groups_id) {
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        if (tempGroup.isPresent())
            return Optional.of(tempGroup.get().getServer());
        return Optional.empty();
    }

    public Optional<Groups> findGroupsByID(Long groups_ID) {
        return groupsRepository.findGroupsByID(groups_ID);
    }

    public List<Groups> underGroup(Long groups_id) {
        Optional<Groups> tempGroup = groupsRepository.findGroupsByID(groups_id);
        if (tempGroup.isPresent())
            return tempGroup.get().getUnderGroups();
        return List.of();
    }

    public Optional<Roles> addRoleForGroup(Long group_id, String role_name, String rights) {
        if (groupsRepository.existsById(group_id)) {
            Roles role = new Roles(
                    role_name,
                    rights
            );
            rolesRepository.save(role);
            groupsRepository.addRoleForGroup(group_id, role.getRole_id());
            return Optional.of(role);
        }
        return Optional.empty();
    }

    public Optional<Roles> addRoleForUsers(List<Long> user_id, Long role_id) {
        if (rolesRepository.findById(role_id).isPresent()) {
            for (Long user : user_id) {
                if (!userRepository.findById(user).get().getRoles().contains(rolesRepository.findById(role_id).get())) {
                    groupsRepository.addRoleForUser(user, role_id);
                }
            }
            return rolesRepository.findById(role_id);
        }
        return Optional.empty();
    }

    public List<Roles> getRoleByGroupAndUser(Long group_id, Long user_id) {
        List<Roles> roles = userRepository.findById(user_id).get().getRoles();
        List<Roles> res = new java.util.ArrayList<>();
        for (Roles user : roles) {
            if (user.getGroups_id().equals(group_id))
                res.add(user);
        }
        return res;
    }

    public Optional<Roles> modifyRightsForRole(Long role_id, String rights) {
        Optional<Roles> roles = rolesRepository.findById(role_id);
        if (roles.isPresent()) {
            groupsRepository.modifyRightsForRole(role_id, rights);
        }
        return roles;
    }

    public Boolean deleteRole(Long role_id) {
        if (rolesRepository.existsById(role_id)) {
            rolesRepository.deleteById(role_id);
            return true;
        }
        return false;
    }

    public Optional<Long> findIDOfDiscoodle() {
        return groupsRepository.findIDOfDiscoodle();
    }

}
