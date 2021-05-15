package com.discoodle.api.repository;

import com.discoodle.api.model.EstablishmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablishmentRequestRepository extends JpaRepository<EstablishmentRequest, Long> {

    @Query("SELECT er FROM EstablishmentRequest er where er.user_id = :user_id")
    List<EstablishmentRequest> getEstablishmentRequestByUser(Long user_id);

    @Query("SELECT er FROM EstablishmentRequest er where er.status = 'COURS' ")
    List<EstablishmentRequest> findAllBeingProcessed();
}
