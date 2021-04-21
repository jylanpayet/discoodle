package com.discoodle.api.repository;

import com.discoodle.api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT room FROM Room room WHERE room.room_id = ?1")
    Optional<Room> findRoomByUUID(String room_id);

    @Modifying
    @Query(value = "INSERT INTO link_rooms_users (user_id, room_id) VALUES (:user_id,:room_id)", nativeQuery = true)
    @Transactional
    void addNewMember(@Param("room_id") String room_id, @Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE r FROM link_rooms_users as r where r.room_id = :room_id AND r.user_id = :user_id", nativeQuery = true)
    void removeMember(@Param("room_id") String room_id, @Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query("UPDATE Room room SET room.link_picture = :link_picture WHERE room.room_id = :room_id")
    int changeLinkPicture(@Param("room_id") String room_id, @Param("link_picture") String link_picture);

    @Transactional
    @Modifying
    @Query("UPDATE Room room SET room.room_admin = :room_admin WHERE room.room_id = :room_id")
    int changeAdmin(@Param("room_id") String room_id, @Param("room_admin") Long room_admin);

}
