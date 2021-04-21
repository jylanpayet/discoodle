package com.discoodle.api.service;

import com.discoodle.api.model.Note;
import com.discoodle.api.repository.NoteRepository;
import com.discoodle.api.request.NoteRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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


}
