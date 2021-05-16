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
        // Check if the user is present and has the Teacher role.
        // Check if the name given to the group does not already exist as well as some checks.
        if(user.isPresent() && user.get().getRole().equals(User.Role.TEACHER) && groupsRepository.findAllGroupsByNameAndDepth(groupsRequest.getName(), 1).isEmpty() && (groupsRequest.getType().equals(Groups.TypeOfGroup.ESTABLISHMENT) || groupsRequest.getType().equals(Groups.TypeOfGroup.FACULTY))){
            // Create a new object EstablishmentRequest.
            EstablishmentRequest er = new EstablishmentRequest(
                    groupsRequest.getUser_id(),
                    groupsRequest.getName(),
                    groupsRequest.getDescription(),
                    groupsRequest.getType(),
                    EstablishmentRequest.Status.COURS
                    );
            // Save the object in the database with Spring.
            establishmentRequestRepository.save(er);
            // Return this new objet.
            return establishmentRequestRepository.findById(er.getEr_id());
        }
        return Optional.empty();
    }

    public Optional<EstablishmentRequest> acceptEstablishmentRequest(Long er_id) {
        Optional<EstablishmentRequest> er=establishmentRequestRepository.findById(er_id);
        // Checks if the request exists and if its status is in progress.
        if(er.isPresent() && er.get().getStatus().equals(EstablishmentRequest.Status.COURS)){
            // Get discoodle id for linked to parent id.
            Optional<Long> discoodle_id = groupsService.findIDOfDiscoodle();

            // Create a new object GroupsRequest.
            GroupsRequest groupsRequest=new GroupsRequest(
                    discoodle_id.get(),
                    er.get().getUser_id(),
                    1,
                    er.get().getName(),
                    er.get().getDescription(),
                    er.get().getType(),
                    ""
            );
            // With this object, we create a new Groups.
            groupsService.createNewGroup(groupsRequest);
            // Set the status of the request to "Acceptée".
            er.get().setStatus(EstablishmentRequest.Status.ACCEPTEE);
            // Save the change.
            establishmentRequestRepository.save(er.get());
            // Return the current object.
            return establishmentRequestRepository.findById(er_id);

        }
        return Optional.empty();
    }

    public Optional<EstablishmentRequest> refuseEstablishmentRequest(Long er_id) {
        Optional<EstablishmentRequest> er=establishmentRequestRepository.findById(er_id);

        // Checks if the request exists and if its status is in progress.
        if(er.isPresent() && er.get().getStatus().equals(EstablishmentRequest.Status.COURS)){
            // Set the status of the request to "Refusée".
            er.get().setStatus(EstablishmentRequest.Status.REFUSEE);
            // Save the change.
            establishmentRequestRepository.save(er.get());
            // Return the current object.
            return establishmentRequestRepository.findById(er_id);
        }
        return Optional.empty();
    }

    public List<EstablishmentRequest> getEstablishmentRequestOfUser(Long user_id) {
        // Check if user exists.
        if(userRepository.existsById(user_id)) {
            // Return the list of Establishment Request of this user.
            return establishmentRequestRepository.getEstablishmentRequestByUser(user_id);
        }
        return List.of();
    }

    public List<EstablishmentRequest> getEstablishmentRequestBeingProcessed() {
        // Return the all Establishement Request of discoodle.
        return establishmentRequestRepository.findAllBeingProcessed();
    }
}
