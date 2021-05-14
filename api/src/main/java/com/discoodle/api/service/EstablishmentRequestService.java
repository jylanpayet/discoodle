package com.discoodle.api.service;

import com.discoodle.api.model.EstablishmentRequest;
import com.discoodle.api.model.Groups;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.EstablishmentRequestRepository;
import com.discoodle.api.repository.GroupsRepository;
import com.discoodle.api.repository.UserRepository;
import com.discoodle.api.request.GroupsRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EstablishmentRequestService {
    public final EstablishmentRequestRepository establishmentRequestRepository;
    public final UserRepository userRepository;
    public final GroupsService groupsService;
    public final GroupsRepository groupsRepository;

    public Optional<EstablishmentRequest> addNewEstablishmentRequest(GroupsRequest groupsRequest) {
        Optional<User> user=userRepository.findById(groupsRequest.getUser_id());
        if(user.isPresent() && user.get().getRole().equals(User.Role.TEACHER) && groupsRepository.findAllGroupsByNameAndDepth(groupsRequest.getName(), 1).isEmpty() && (groupsRequest.getType().equals(Groups.TypeOfGroup.ESTABLISHMENT) || groupsRequest.getType().equals(Groups.TypeOfGroup.FACULTY))){
            EstablishmentRequest er = new EstablishmentRequest(
                    groupsRequest.getUser_id(),
                    groupsRequest.getName(),
                    groupsRequest.getDescription(),
                    groupsRequest.getType(),
                    EstablishmentRequest.Status.COURS
                    );
            establishmentRequestRepository.save(er);
            return establishmentRequestRepository.findById(er.getEr_id());
        }
        return Optional.empty();
    }

    public Optional<EstablishmentRequest> acceptEstablishmentRequest(Long er_id) {
        Optional<EstablishmentRequest> er=establishmentRequestRepository.findById(er_id);
        if(er.isPresent() && er.get().getStatus().equals(EstablishmentRequest.Status.COURS)){
            Optional<Long> discoodle_id = groupsService.findIDOfDiscoodle();
            GroupsRequest groupsRequest=new GroupsRequest(
                    discoodle_id.get(),
                    er.get().getUser_id(),
                    1,
                    er.get().getName(),
                    er.get().getDescription(),
                    er.get().getType(),
                    ""
            );
            groupsService.createNewGroup(groupsRequest);
            er.get().setStatus(EstablishmentRequest.Status.ACCEPTEE);
            establishmentRequestRepository.save(er.get());
            return establishmentRequestRepository.findById(er_id);

        }
        return Optional.empty();
    }

    public Optional<EstablishmentRequest> refuseEstablishmentRequest(Long er_id) {
        Optional<EstablishmentRequest> er=establishmentRequestRepository.findById(er_id);
        if(er.isPresent() && er.get().getStatus().equals(EstablishmentRequest.Status.COURS)){
            er.get().setStatus(EstablishmentRequest.Status.REFUSEE);
            establishmentRequestRepository.save(er.get());
            return establishmentRequestRepository.findById(er_id);
        }
        return Optional.empty();
    }

    public List<EstablishmentRequest> getEstablishmentRequestOfUser(Long user_id) {
        if(userRepository.existsById(user_id)) {
            return establishmentRequestRepository.getEstablishmentRequestByUser(user_id);
        }
        return List.of();
    }

    public List<EstablishmentRequest> getEstablishmentRequestBeingProcessed() {
        return establishmentRequestRepository.findAllBeingProcessed();
    }
}
