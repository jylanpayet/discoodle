package com.discoodle.api.controller;
import com.discoodle.api.model.Note;
import com.discoodle.api.request.NoteRequest;
import com.discoodle.api.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@AllArgsConstructor
@RestController
public class NoteController {

    private final NoteService noteService;

    @PostMapping(path = "/api/note/addNewNote")
    public Optional<Note> addNewNote(@RequestBody NoteRequest request) {
        return noteService.createNewNote(request);
    }



}
