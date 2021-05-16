package com.discoodle.api.repository;

import com.discoodle.api.model.Room;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO link_rooms_users (user_id, room_id) VALUES (:user_id,:room_id)", nativeQuery = true)
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

    @Transactional
    @Modifying
    @Query("UPDATE Room room SET room.room_name = :new_name WHERE room.room_id = :room_id")
    void renameRoom(@Param("room_id") String room_id, @Param("new_name") String new_name);

    @Query("SELECT room.users FROM Room room WHERE room.room_id = :room_id")
    List<User> getUserOfRoom(String room_id);

    @Query("SELECT room.room_admin FROM Room room WHERE room.room_id = :room_id")
    Long getAdminOfRoom(String room_id);
}
