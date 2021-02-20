package com.discoodle.api.configuration;

import com.discoodle.api.model.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class DiscoodleJsonFileWriter {


    public static void runWriter(ChatMessage msg, String roomUUID) {

        System.out.println("------" + roomUUID);

        LinkedList<ChatMessage> chatMessage = new LinkedList<>();
        StringBuilder jsonContent = new StringBuilder();

        try {
            File myObj = new File(String.format("static/common/json/%s.json", roomUUID));
            if (myObj.createNewFile()) {
                PrintWriter writer = new PrintWriter(myObj);
                writer.write("[\n\n]");
                writer.close();
            }
            Path path = Paths.get(String.format("static/common/json/%s.json", roomUUID));
            Gson gson = new Gson();

            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                jsonContent.append(data);
            }
            myReader.close();
            chatMessage = gson.fromJson(String.valueOf(jsonContent), new TypeToken<LinkedList<ChatMessage>>() {}.getType());

            if (chatMessage == null)
                chatMessage = new LinkedList<>();

            chatMessage.addFirst(msg);

            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(gson.toJsonTree(chatMessage), writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
