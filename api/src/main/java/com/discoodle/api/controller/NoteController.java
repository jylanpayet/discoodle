package com.discoodle.api.controller;

import com.discoodle.api.model.Note;
import com.discoodle.api.request.NoteRequest;
import com.discoodle.api.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@RestController
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/api/note/addNewNote")
    public Optional<Note> addNewNote(@RequestBody NoteRequest request) {
        return noteService.createNewNote(request);
    }

    @DeleteMapping("/api/note/{note_id}")
    public void deleteNoteById(@PathVariable("note_id") Long note_id) {
        noteService.deleteNoteById(note_id);
    }

    @GetMapping("api/note/group_id/{group_id}")
    @ResponseBody
    public List<Note> getAllNoteByGroupId(@PathVariable("group_id") Long group_id) {
       return noteService.getAllNoteByGroupId(group_id);
    }

    @GetMapping("api/note/user_id/{user_id}")
    @ResponseBody
    public List<Note> getAllNoteByUserId(@PathVariable("user_id") Long user_id) {
        return noteService.getAllNoteByUserId(user_id);
    }

    @GetMapping("api/note/titre/{titre}")
    @ResponseBody
    public List<Note> getAllNoteByTitre(@PathVariable("titre") String titre) {
        return noteService.getAllNoteByTitre(titre);
    }


}
