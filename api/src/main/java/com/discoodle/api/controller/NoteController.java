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

    @DeleteMapping("/api/note/deleteNoteById")
    public void deleteNoteById(@RequestParam("note_id") Long note_id) {
        noteService.deleteNoteById(note_id);
    }

    @DeleteMapping("api/note/deleteAllNoteByGroupId")
    public void deleteAllNoteByGroupId(@RequestParam("group_id") Long group_id) {
        noteService.deleteAllNoteByGroupId(group_id);
    }

    @DeleteMapping("api/note/deleteAllNoteByUserId")
    public void deleteAllNoteByUserId(@RequestParam("user_id") Long user_id) {
        noteService.deleteAllNoteByUserId(user_id);
    }

    @DeleteMapping("api/note/deleteAllNoteByTitre")
    public void deleteAllNoteByTitre(@RequestParam("group_id") Long group_id,@RequestParam("titre") String titre) {
        noteService.deleteAllNoteByTitre(group_id,titre);
    }

    @GetMapping("api/note/getAllNoteByGroupId")
    @ResponseBody
    public List<Note> getAllNoteByGroupId(@RequestParam("group_id") Long group_id) {
        return noteService.getAllNoteByGroupId(group_id);
    }

    @GetMapping("api/note/getAllNoteByUserId")
    @ResponseBody
    public List<Note> getAllNoteByUserId(@RequestParam("user_id") Long user_id) {
        return noteService.getAllNoteByUserId(user_id);
    }

    @GetMapping("api/note/getAllNoteByTitre")
    @ResponseBody
    public List<Note> getAllNoteByTitre(@RequestParam("group_id") Long group_id,@RequestParam("titre") String titre) {
        return noteService.getAllNoteByTitre(group_id,titre);
    }

    @GetMapping("api/note/getNoteByNoteId")
    @ResponseBody
    public Optional<Note> getNoteByNoteId(@RequestParam("note_id") Long note_id) {
        return noteService.getNoteByNoteId(note_id);
    }

    @GetMapping("api/note/getUserNoteByGroupId")
    @ResponseBody
    public List<Note> getUserNoteByGroupId(@RequestParam("group_id") Long group_id, @RequestParam("user_id") Long user_id){
        return noteService.getUserNoteByGroupId(group_id,user_id);
    }


    @PostMapping("api/note/editNote")
    public void editNote(@RequestParam(name = "group_id") Long group_id,
                         @RequestParam(name = "note_id") Long note_id,
                         @RequestParam (value = "note") double note) {
        noteService.editNote(group_id,note_id,note);
    }
}
