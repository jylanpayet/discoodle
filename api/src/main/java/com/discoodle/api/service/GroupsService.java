package com.discoodle.api.service;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.GroupRights;
import com.discoodle.api.model.Server;
import com.discoodle.api.request.*;
import com.discoodle.api.model.Groups;
import com.discoodle.api.repository.GroupRightsRepository;
import com.discoodle.api.repository.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@Service
@AllArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final GroupRightsRepository rightsRepository;
    private final ServerService serverService;

    public Groups createNewGroup(GroupsRequest request) {
        GroupRights rights = new GroupRights(false, false, false);
        rights = rightsRepository.save(rights);
        Groups group = new Groups(
                request.getParent_id(),
                request.getDepth(),
                request.getName(),
                request.getDescription(),
                request.getType()
        );
        Groups finalGroup = groupsRepository.save(group);
        groupsRepository.addNewRightsInGroup(finalGroup.getGroups_id(), rights.getRightsId());
        groupsRepository.addNewGroupsInGroup(request.getParent_id(), finalGroup.getGroups_id());
        groupsRepository.addNewMemberInGroup(request.getUser_id(), finalGroup.getGroups_id());

        if (finalGroup.getType().equals(Groups.TypeOfGroup.SUBJECTS)) {
            Server server=serverService.createNewServ("Serveur de "+finalGroup.getName(),List.of(request.getUser_id()));
            groupsRepository.addNewServInGroup(finalGroup.getGroups_id(),server.getServer_id());
            try {
                File dossier = new File((String.format("%sstatic/common/groups/%d", ApiApplication.RESSOURCES, finalGroup.getGroups_id())));
                dossier.mkdirs();
                if (dossier.exists() && dossier.isDirectory()) {
                    File fichier = new File((String.format("%sstatic/common/groups/%d/%d.json", ApiApplication.RESSOURCES, finalGroup.getGroups_id(), finalGroup.getGroups_id())));
                    fichier.createNewFile();
                }
            } catch (Exception e) {
                System.out.println("Dossier du groups non crée !");
            }
        }
        return finalGroup;
    }

    public boolean editRights(GroupRightsRequest request) {
        try {
            GroupRights ofParent = groupsRepository.findById(groupsRepository.findParentOfGroup(request.getGroupId())).get().getGroupRights();
            GroupRights r = groupsRepository.findGroupsByID(request.getGroupId()).get().getGroupRights();
            if (ofParent.isCanAddUser())
                rightsRepository.updateRightsAdd(r.getRightsId(), request.isAddUser());
            if (ofParent.isCanDeleteUser())
                rightsRepository.updateRightsDelete(r.getRightsId(), request.isDeleteUser());
            if (ofParent.isCanModify())
                rightsRepository.updateRightsModify(r.getRightsId(), request.isModify());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean editGroup(EditGroupRequest request) {
        try {
            groupsRepository.updateNameAndDescGroup(request.getGroups_id(), request.getName(), request.getDescription());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void editFileGroup(Long groups_id,String text) {
        try {
            File path = new File(String.format("%sstatic/common/groups/%d/%d.json", ApiApplication.RESSOURCES, groups_id, groups_id));
            Files.writeString(Paths.get(String.valueOf(path)), text+"\n");
        } catch (Exception e) {
            System.out.println("erreur, fichier non édité");
            e.printStackTrace();
        }
    }

    public void deleteGroupByID(Long groups_ID) {
        groupsRepository.deleteById(groups_ID);
    }

    public void addNewMemberInGroup(NMGroupRequest request){
        groupsRepository.addNewMemberInGroup(request.getUser_id(), request.getGroup_id());
    }
}
