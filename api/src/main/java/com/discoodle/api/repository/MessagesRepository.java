package com.discoodle.api.repository;

import com.discoodle.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface MessagesRepository extends JpaRepository<Message, Long> {

   @Query(value = "SELECT messages FROM Message messages WHERE messages.conv_uuid=?1")
   List<Message> findMessagesOfRoom(String conv_uuid);

   @Transactional
   @Modifying
   @Query("UPDATE Message messages SET messages.pinned = true WHERE messages.message_id = :message_id")
   void pinMessage(Long message_id);

   @Transactional
   @Modifying
   @Query("UPDATE Message messages SET messages.pinned = false WHERE messages.message_id = :message_id")
   void unpinMessage(Long message_id);

   @Transactional
   @Modifying
   @Query("UPDATE Message messages SET messages.content = :content WHERE messages.message_id = :message_id")
   void editMessage(Long message_id, String content);

   @Transactional
   @Modifying
   @Query("UPDATE Message messages SET messages.edited = true WHERE messages.message_id = :message_id")
   void setEdited(Long message_id);
}
