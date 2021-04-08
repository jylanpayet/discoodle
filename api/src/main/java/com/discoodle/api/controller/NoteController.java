package com.discoodle.api.controller;

import com.discoodle.api.ApiApplication;
import com.discoodle.api.model.Groups;
import com.discoodle.api.model.Message;
import com.discoodle.api.model.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileReader;
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
        try {
            File file = new File(String.format("%sstatic/common/groups/%d/Notes_%d.json", ApiApplication.RESSOURCES, group_id, group_id));
            file.createNewFile();
            JsonReader reader = new JsonReader(new FileReader(String.format("%sstatic/common/groups/%d/Notes_%d.json", ApiApplication.RESSOURCES, group_id, group_id)));
            LinkedList<Note> noteLinkedList = gson.fromJson(reader, new TypeToken<LinkedList<Message>>() {
            }.getType());
            noteLinkedList.addLast(note);
            Path path = Paths.get(String.format("%sstatic/common/groups/%d/Notes_%d.json", ApiApplication.RESSOURCES, group_id, group_id));
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(gson.toJsonTree(noteLinkedList), writer);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("fichier json non mis à jour");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("fichier non éditier");
        }
    }
}
