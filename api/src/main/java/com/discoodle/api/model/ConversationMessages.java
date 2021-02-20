package com.discoodle.api.model;

import java.util.LinkedList;

public class ConversationMessages {
   private LinkedList<ChatMessage> chatMessages = new LinkedList<>();

   public ConversationMessages(LinkedList<ChatMessage> chatMessages) {
      this.chatMessages = chatMessages;
   }

   public ConversationMessages() {
   }

   public LinkedList<ChatMessage> getChatMessages() {
      return chatMessages;
   }

   public void setChatMessages(LinkedList<ChatMessage> chatMessages) {
      this.chatMessages = chatMessages;
   }
}
