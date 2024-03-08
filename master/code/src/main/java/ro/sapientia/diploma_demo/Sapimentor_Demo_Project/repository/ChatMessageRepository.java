package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessage;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatId(String chatId);

    @Modifying
    @Query("Delete FROM ChatMessage c WHERE c.senderId = :userId OR c.recipientId = :userId")
    void deleteByUserId(Long userId);
}
