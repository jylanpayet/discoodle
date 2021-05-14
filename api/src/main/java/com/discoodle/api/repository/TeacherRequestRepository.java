package com.discoodle.api.repository;

import com.discoodle.api.model.TeacherRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRequestRepository extends JpaRepository<TeacherRequest, Long> {

    @Query("SELECT tr FROM TeacherRequest tr where tr.user_id = :user_id")
    Optional<TeacherRequest> getTeacherRequestByUser(Long user_id);

}
