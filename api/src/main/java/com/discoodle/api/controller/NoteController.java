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

    @DeleteMapping("/api/note/deleteBNI{note_id}")
    public void deleteNoteById(@PathVariable("note_id") Long note_id) {
        noteService.deleteNoteById(note_id);
    }

    @DeleteMapping("api/note/deleteBGI/{group_id}")
    public void deleteAllNoteByGroupId(@PathVariable("group_id") Long group_id) {
        noteService.deleteAllNoteByGroupId(group_id);
    }

    @DeleteMapping("api/note/deleteBUI/{user_id}")
    public void deleteAllNoteByUserId(@PathVariable("user_id") Long user_id) {
        noteService.deleteAllNoteByUserId(user_id);
    }

    @DeleteMapping("api/note/deleteBT/{group_id}/{titre}")
    public void deleteAllNoteByTitre(@PathVariable("group_id") Long group_id,@PathVariable("titre") String titre) {
        noteService.deleteAllNoteByTitre(group_id,titre);
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

    @GetMapping("api/note/titre/{group_id}/{titre}")
    @ResponseBody
    public List<Note> getAllNoteByTitre(@PathVariable("group_id") Long group_id,@PathVariable("titre") String titre) {
        return noteService.getAllNoteByTitre(group_id,titre);
    }

    @GetMapping("api/note/note_id/{note_id}")
    @ResponseBody
    public Optional<Note> getNoteByNoteId(@PathVariable("note_id") Long note_id) {
        return noteService.getNoteByNoteId(note_id);
    }


    @PostMapping("api/note/editNote/{group_id}/{note_id}")
    public void editNote(@PathVariable(name = "group_id") Long group_id,
                         @PathVariable(name = "note_id") Long note_id,
                         @RequestParam (value = "note") double note) {
        noteService.editNote(group_id,note_id,note);
    }
}
