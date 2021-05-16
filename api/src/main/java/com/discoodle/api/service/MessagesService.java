package com.discoodle.api.service;

import com.discoodle.api.model.Message;
import com.discoodle.api.repository.MessagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MessagesService {

   private final MessagesRepository messageRepository;

   public List<Message> getMessagesOfRoom(String room_uuid) {
      return messageRepository.getMessagesOfRoom(room_uuid);
   }

   public Message sendMessage(Message message) {
      return messageRepository.save(message);
   }

   public void pinMessage(Long message_id) {
      if(messageRepository.existsById(message_id))
         messageRepository.pinMessage(message_id);
   }

   public void unpinMessage(Long message_id) {
      if(messageRepository.existsById(message_id))
         messageRepository.unpinMessage(message_id);
   }

   public void deleteMessage(Long message_id) {
      if(messageRepository.existsById(message_id))
         messageRepository.deleteById(message_id);
   }

   public void editMessage(Long message_id, String content) {
      if(messageRepository.existsById(message_id))
         messageRepository.editMessage(message_id, content);
   }

   public void setEdited(Long message_id) {
      if(messageRepository.existsById(message_id))
         messageRepository.setEdited(message_id);
   }
}
