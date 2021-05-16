package com.discoodle.api.repository;

import com.discoodle.api.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

    @Modifying
    @Query(value = "insert into link_server_room (server_id, room_id) VALUES (:server_id,:room_id)", nativeQuery = true)
    @Transactional
    void addNewRoomInServ(@Param("server_id") Long server_id, @Param("room_id") String room_id);

    @Modifying
    @Query(value = "insert into link_server_users (server_id, user_id) VALUES (:server_id,:user_id)", nativeQuery = true)
    @Transactional
    void addNewMember(@Param("server_id") Long server_id, @Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE r FROM link_server_users as r where r.server_id = :server_id AND r.user_id = :user_id", nativeQuery = true)
    void removeMember(@Param("server_id") Long server_id, @Param("user_id") Long user_id);

}
