package com.discoodle.api.repository;

import com.discoodle.api.model.Message;
import com.discoodle.api.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    @Query(value = "SELECT role FROM Roles role WHERE role.name = :name AND role.role_id=:role_id ")
    Optional<Roles> getRolesByNameAnAndGroupsId(String name, Long role_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE r FROM link_role_to_users as r where r.user_id = :user_id AND r.role_id = :role_id", nativeQuery = true)
    void deleteLinkRoleToUser(@Param("user_id") Long user_id, @Param("role_id") Long role_id);
}
