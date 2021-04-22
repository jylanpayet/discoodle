package com.discoodle.api.repository;

import com.discoodle.api.model.Note;
import com.discoodle.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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


}
