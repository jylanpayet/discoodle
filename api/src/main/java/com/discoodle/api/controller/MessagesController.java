package com.discoodle.api.controller;
import com.discoodle.api.model.Message;
import com.discoodle.api.model.Conversation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

@RestController("/api")
public class MessagesController {

   @GetMapping("api/messages")
   public Conversation getMessages(@RequestParam(value = "uuid", defaultValue = "messages") String uuid) {
      Gson gson = new Gson();

      Conversation conversation= new Conversation();
      StringBuilder jsonContent = new StringBuilder();

      try {
         File myObj = new File("static/common/json/"+uuid+".json");
         if (!myObj.exists()) {
            myObj.createNewFile();
            PrintWriter writer = new PrintWriter(myObj);
            writer.write("[\n\t\n]");
            writer.close();
            return conversation;
         }
         Scanner myReader = new Scanner(myObj);
         while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            jsonContent.append(data);
         }
         myReader.close();
         conversation.setMessages(gson.fromJson(jsonContent.toString(), new TypeToken<LinkedList<Message>>(){}.getType()));
      } catch (IOException e) {
         e.printStackTrace();
      }

      return conversation;
   }

}
