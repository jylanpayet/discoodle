package com.discoodle.api.repository;

import com.discoodle.api.model.Note;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT note FROM Note note where note.group_id= ?1")
    List<Note> getAllNoteByGroupId(Long group_id);

    @Query("SELECT note FROM Note note where note.user_id= ?1")
    List<Note> getAllNoteByUserId(Long user_id);

    @Query("SELECT note FROM Note note where note.titre= ?1")
    List<Note> getAllNoteByTitre(String titre);

    @Transactional
    @Modifying
    @Query("DELETE FROM Note note WHERE note.group_id= :group_id")
    void deleteAllNoteByGroupId(@Param("group_id") Long group_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Note note WHERE note.user_id= :user_id")
    void deleteAllNoteByUserId(@Param("user_id") Long user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Note note WHERE note.titre= :titre")
    void deleteAllNoteByTitre(@Param("titre") String titre);

    @Transactional
    @Modifying
    @Query(value = "UPDATE note n SET n.note= :note WHERE n.group_id= :group_id AND n.note_id= :note_id", nativeQuery = true)
    void editNote(@Param("group_id") Long group_id, @Param("note_id") Long note_id, @Param("note") double note);
}
