package com.discoodle.api.service;

import com.discoodle.api.model.TeacherRequest;
import com.discoodle.api.model.User;
import com.discoodle.api.repository.TeacherRequestRepository;
import com.discoodle.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TeacherRequestService {

    TeacherRequestRepository teacherRequestRepository;
    UserRepository userRepository;

    public Optional<TeacherRequest> addNewTeacherRequest(Long user_id) {
        Optional<User> user=userRepository.findById(user_id);
        Optional <TeacherRequest> test = teacherRequestRepository.getTeacherRequestByUser(user_id);

        //If the conditions are not met, return an empty element or the already existing request.
        if(test.isPresent() && user.isPresent()){
            if ((test.get().getStatus().equals(TeacherRequest.Status.COURS)) || user.get().getRole().equals(User.Role.TEACHER)){
                return test;
            }
            if(test.get().getStatus().equals(TeacherRequest.Status.REFUSEE)){
                test.get().setStatus(TeacherRequest.Status.COURS);
                teacherRequestRepository.save(test.get());
                return teacherRequestRepository.getTeacherRequestByUser(user_id);
            }
        }

        // Create a new object TeacherRequest.
        TeacherRequest teacherRequest = new TeacherRequest(
                user_id,
                TeacherRequest.Status.COURS
        );
        // Save the object in the database with Spring.
        teacherRequest = teacherRequestRepository.save(teacherRequest);

        // Return this teacherRequest.
        return teacherRequestRepository.findById(teacherRequest.getTr_id());
    }


    public Optional<TeacherRequest> acceptTeacherRequest(Long user_id) {
        Optional<User> user=userRepository.findById(user_id);
        // Check if user exists.
        if(user.isPresent()){
            // Modify his role and accept the request.
            user.get().setRole(User.Role.TEACHER);
            Optional<TeacherRequest> teacherRequest= teacherRequestRepository.getTeacherRequestByUser(user_id);
            teacherRequest.get().setStatus(TeacherRequest.Status.ACCEPTEE);
            teacherRequestRepository.save(teacherRequest.get());

            // Return his request.
            return teacherRequestRepository.getTeacherRequestByUser(user_id);
        }
        return Optional.empty();
    }

    public Optional<TeacherRequest> refuseTeacherRequest(Long user_id) {
        Optional<User> user=userRepository.findById(user_id);
        // Check if user exists.
        if(user.isPresent()){
            // Refuse the request and return this.
            Optional<TeacherRequest> teacherRequest= teacherRequestRepository.getTeacherRequestByUser(user_id);
            teacherRequest.get().setStatus(TeacherRequest.Status.REFUSEE);
            teacherRequestRepository.save(teacherRequest.get());

            // Return his request.
            return teacherRequestRepository.getTeacherRequestByUser(user_id);
        }
        return Optional.empty();
    }

    public Optional<TeacherRequest> getTeacherRequestOfUser(Long user_id) {
        Optional<TeacherRequest> tr = teacherRequestRepository.getTeacherRequestByUser(user_id);
        // Check if user exists.
        if(tr.isPresent()){
            // Return his request.
            return tr;
        }
        return Optional.empty();
    }

    public List<TeacherRequest> getTeacherRequestBeingProcessed() {
        return teacherRequestRepository.findAllBeingProcessed();
    }
}
