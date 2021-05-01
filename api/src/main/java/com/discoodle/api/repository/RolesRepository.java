package com.discoodle.api.repository;

import com.discoodle.api.model.Message;
import com.discoodle.api.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Query(value = "SELECT role FROM Roles role WHERE role.name = :name AND role.groups_id=:group_id ")
    Optional<Roles> getRolesByNameAnAndGroupsId(String name, Long group_id);

}
