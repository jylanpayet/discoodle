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
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT room FROM Room room where room.room_id = ?1")
    Optional<Room> findRoomByUUID(String room_id);

    @Modifying
    @Query(value = "insert into link_rooms_users (user_id, room_id) VALUES (:user_id,:room_id)", nativeQuery = true)
    @Transactional
    void addNewMembers(@Param("user_id") Integer user_id, @Param("room_id") String room_id);
}
