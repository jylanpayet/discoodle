package com.discoodle.api.service;

import com.discoodle.api.model.GroupRights;
import com.discoodle.api.model.GroupRightsRequest;
import com.discoodle.api.model.Groups;
import com.discoodle.api.model.GroupsRequest;
import com.discoodle.api.repository.GroupRightsRepository;
import com.discoodle.api.repository.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;
    private final GroupRightsRepository rightsRepository;

    public Groups createNewGroup(GroupsRequest request) {
        GroupRights rights=new GroupRights(false, false, false);
        rights = rightsRepository.save(rights);
        Groups group = new Groups(
                request.getDepth(),
                request.getName(),
                request.getDescription(),
                request.getType()
        );
        Groups finalGroup = groupsRepository.save(group);
        groupsRepository.addNewRightsInGroup(finalGroup.getGroups_id(),rights.getRightsId());
        groupsRepository.addNewGroupsInGroup(request.getParent_id(),finalGroup.getGroups_id());
        groupsRepository.addNewMemberInGroup(request.getUser_id(),finalGroup.getGroups_id());
        return finalGroup;
    }

    public boolean editRights(GroupRightsRequest request){
        GroupRights ofParent=groupsRepository.findGroupsById(groupsRepository.findParentOfGroup(request.getGroupId())).get().getGroupRights();
        GroupRights r=groupsRepository.findGroupsById(request.getGroupId()).get().getGroupRights();
        if(ofParent.isCanAddUser())
            rightsRepository.updateRightsAdd(r.getRightsId(),request.isAddUser());
        if(ofParent.isCanDeleteUser())
            rightsRepository.updateRightsDelete(r.getRightsId(),request.isDeleteUser());
        if(ofParent.isCanModify())
            rightsRepository.updateRightsModify(r.getRightsId(),request.isModify());
        return true;
    }
}
