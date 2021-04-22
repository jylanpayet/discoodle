package com.discoodle.api.service;

import com.discoodle.api.model.Note;
import com.discoodle.api.repository.NoteRepository;
import com.discoodle.api.request.NoteRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class NoteService {

    public final NoteRepository noteRepository;

    public Optional<Note> createNewNote(NoteRequest request) {
        Note note = new Note(
                request.getUser_id(),
                request.getGroup_id(),
                request.getNote(),
                request.getCoef(),
                request.getTitre()
        );
        Note finalNote = noteRepository.save(note);

        return Optional.of(finalNote);
    }

    public void deleteNoteById(Long note_id) {

        noteRepository.deleteById(note_id);
    }

    public List<Note> getAllNoteByGroupId(Long group_id){
        return noteRepository.getAllNoteByGroupId(group_id);
    }

    public List<Note> getAllNoteByUserId(Long user_id){
        return noteRepository.getAllNoteByUserId(user_id);
    }

    public List<Note>getAllNoteByTitre(String titre){
        return noteRepository.getAllNoteByTitre(titre);
    }

}
