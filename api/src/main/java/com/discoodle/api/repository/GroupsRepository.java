package com.discoodle.api.repository;

import com.discoodle.api.model.Groups;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {


    @Query("SELECT group FROM Groups group where group.groups_id = ?1")
    Optional<Groups> findGroupsByID(Long user_ID);

    @Query(value = "SELECT groups FROM Groups groups where groups.depth=?1")
    Optional<Groups> findAllGroupsByDepth(Integer depth);

    @Query(value = "SELECT groups FROM Groups groups where groups.name=?1")
    Optional<Groups> findAllGroupsByName(String name);

    @Query(value = "SELECT groups FROM Groups groups where groups.type=?1")
    Optional<Groups> findAllGroupsByType(String type);

    @Transactional
    @Modifying
    @Query(value = "UPDATE groups g SET g.name=?2,g.description=?3 WHERE g.groups_id=?1", nativeQuery = true)
    Optional<Groups> updateNameAndDescGroup(@Param("groups_id") Long id,@Param("name") String name,@Param("description") String description);

    @Query(value = "SELECT groups_id FROM link_groups_to_group g where g.son_id=?1", nativeQuery = true)
    Long findParentOfGroup(Long son_id);

    @Modifying
    @Query(value = "insert into link_groups_to_user (user_id, groups_id) VALUES (:user_id,:groups_id)", nativeQuery = true)
    @Transactional
    void addNewMemberInGroup(@Param("user_id") Long user_ID, @Param("groups_id") Long groups_ID);

    @Modifying
    @Query(value = "insert into link_groups_to_group (groups_id, son_id) VALUES (:groups_id,:son_id)", nativeQuery = true)
    @Transactional
    void addNewGroupsInGroup(@Param("groups_id") Long groups_ID, @Param("son_id") Long son_ID);

    @Modifying
    @Query(value = "UPDATE groups g SET g.groups_rights_id=?2 WHERE g.groups_id=?1", nativeQuery = true)
    @Transactional
    void addNewRightsInGroup(@Param("groups_id") Long groups_ID, @Param("rights_id") Long right_ID);

    @Modifying
    @Query(value = "insert into link_groups_to_server (groups_id, server_id) VALUES (:groups_id,:server_id)", nativeQuery = true)
    @Transactional
    void addNewServInGroup(@Param("groups_id") Long groups_id, @Param("server_id") Long server_id);
}
