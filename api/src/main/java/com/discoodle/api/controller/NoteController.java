package com.discoodle.api.controller;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.Note;
import com.discoodle.api.request.NoteRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;


@AllArgsConstructor
@RestController
public class NoteController {


    @PostMapping(path = "/api/addNewNote/{group_id}")
    public void addNewNote(@RequestBody Note note, @PathVariable Long group_id) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String link=String.format("%sstatic/common/groups/%d/%d.json", ApiApplication.RESSOURCES, group_id, group_id);

        try {
            File file = new File(link);
            if (!file.exists()) {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file);
                writer.write("[\n\n]");
                writer.close();
            }

            JsonReader reader = new JsonReader(new FileReader(link));
            LinkedList<Note> noteLinkedList = gson.fromJson(reader, new TypeToken<LinkedList<Note>>() {}.getType());
            note.setNote_id((long) noteLinkedList.size());
            noteLinkedList.add(note);
            Path path = Paths.get(link);

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(gson.toJsonTree(noteLinkedList), writer);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fichier json non mis à jour.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("addNewNote error");
        }
    }

    @PostMapping(path = "/api/deleteNote/{group_id}/{note_id}")
    public void deleteNote(@PathVariable(name = "group_id") Long group_id, @PathVariable(name = "note_id") Long note_id) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String link=String.format("%sstatic/common/groups/%d/%d.json", ApiApplication.RESSOURCES, group_id, group_id);

        try {
            JsonReader reader = new JsonReader(new FileReader(link));
            LinkedList<Note> noteLinkedList = gson.fromJson(reader, new TypeToken<LinkedList<Note>>() {}.getType());

            for (Note n : noteLinkedList) {
                if (n.getNote_id() == (note_id)) {
                    noteLinkedList.remove(n);
                    System.out.println(n);
                    Path path = Paths.get(link);

                    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                        gson.toJson(gson.toJsonTree(noteLinkedList), writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Note non supprimée.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("deleteNote error");
        }
    }

    @PostMapping(path = "/api/editNote/{group_id}/{note_id}")
    public void editNote(@PathVariable(name = "group_id") Long group_id, @PathVariable(name = "note_id") Long note_id, @RequestBody NoteRequest note) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String link=String.format("%sstatic/common/groups/%d/%d.json", ApiApplication.RESSOURCES, group_id, group_id);

        try {
            JsonReader reader = new JsonReader(new FileReader(link));
            LinkedList<Note> noteLinkedList = gson.fromJson(reader, new TypeToken<LinkedList<Note>>() {}.getType());
            for (Note n : noteLinkedList){
                if(n.getNote_id() == (note_id)){
                    n.setNote(note.getNote());
                    System.out.println(n);
                    Path path = Paths.get(link);

                    try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                        gson.toJson(gson.toJsonTree(noteLinkedList), writer);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Note non modifiée.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("editNote file error");
        }
    }
}
