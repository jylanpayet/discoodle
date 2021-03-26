package com.discoodle.api.service;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.GroupsRequest;
import com.discoodle.api.repository.GroupsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GroupsService {
    private final GroupsRepository groupsRepository;

    public Groups createNewGroup(GroupsRequest request) {
        Groups group = new Groups(
                request.getDepth(),
                request.getName(),
                request.getDescription(),
                request.getType()
        );
        Groups finalGroup = groupsRepository.save(group);
        groupsRepository.addNewGroupsInGroup(request.getParent_id(),finalGroup.getGroups_id());
        groupsRepository.addNewMemberInGroup(request.getUser_id(),finalGroup.getGroups_id());
        return finalGroup;
    }
}
