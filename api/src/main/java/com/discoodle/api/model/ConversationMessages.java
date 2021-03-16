package com.discoodle.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Getter
@Setter
@ToString

public class ConversationMessages {
   private LinkedList<ChatMessage> chatMessages;

   public ConversationMessages(LinkedList<ChatMessage> chatMessages) {
      this.chatMessages = chatMessages;
   }

   public ConversationMessages() {
      this(new LinkedList<>());
   }
}
