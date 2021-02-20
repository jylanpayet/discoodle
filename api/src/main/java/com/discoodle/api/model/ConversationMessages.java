package com.discoodle.api.model;

import java.util.LinkedList;

public class ConversationMessages {
   private LinkedList<ChatMessage> chatMessages;

   public ConversationMessages(LinkedList<ChatMessage> chatMessages) {
      this.chatMessages = chatMessages;
   }

   public ConversationMessages() {
      this(new LinkedList<>());
   }

   public LinkedList<ChatMessage> getChatMessages() {
      return chatMessages;
   }

   public void setChatMessages(LinkedList<ChatMessage> chatMessages) {
      this.chatMessages = chatMessages;
   }
}
