package com.discoodle.api.controller;

import com.discoodle.api.model.EstablishmentRequest;
import com.discoodle.api.request.GroupsRequest;
import com.discoodle.api.service.EstablishmentRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/establishmentRequest")
@AllArgsConstructor
public class EstablishmentRequestController {
    public final EstablishmentRequestService establishmentRequestService;

    @PostMapping("/addEstablishmentRequest")
    public Optional<EstablishmentRequest> addNewEstablishmentRequest(@RequestBody GroupsRequest groupsRequest) {
        return establishmentRequestService.addNewEstablishmentRequest(groupsRequest);
    }

    @PutMapping("/acceptEstablishmentRequest")
    public Optional<EstablishmentRequest> acceptEstablishmentRequest(@RequestParam("er_id") Long er_id) {
        return establishmentRequestService.acceptEstablishmentRequest(er_id);
    }

    @PutMapping("/refuseEstablishmentRequest")
    public Optional<EstablishmentRequest> refuseEstablishmentRequest(@RequestParam("er_id") Long er_id) {
        return establishmentRequestService.refuseEstablishmentRequest(er_id);
    }
    @GetMapping("/getEstablishmentRequestOfUser")
    public List<EstablishmentRequest> getEstablishmentRequestOfUser(@RequestParam("user_id") Long user_id){
        return establishmentRequestService.getEstablishmentRequestOfUser(user_id);
    }

    @GetMapping("/getEstablishmentRequestBeingProcessed")
    public List<EstablishmentRequest> getEstablishmentRequestBeingProcessed(){
        return establishmentRequestService.getEstablishmentRequestBeingProcessed();
    }
}
