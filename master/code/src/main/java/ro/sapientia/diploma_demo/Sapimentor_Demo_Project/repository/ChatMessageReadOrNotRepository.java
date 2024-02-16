package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessageReadOrNot;

import java.util.List;

public interface ChatMessageReadOrNotRepository extends JpaRepository<ChatMessageReadOrNot, Long> {
    ChatMessageReadOrNot findByChatIdAndSenderId(String chatId, Long senderId);

    @Query("SELECT c.senderId, c.recipientId, c.readOrNot FROM ChatMessageReadOrNot c WHERE c.recipientId = :recipientId")
    List<Object[]> findByRecipientId(Long recipientId);

    ChatMessageReadOrNot findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    @Query("SELECT CASE WHEN COUNT(cmron) > 0 THEN true ELSE false END FROM ChatMessageReadOrNot cmron WHERE cmron.recipientId = :userId AND cmron.readOrNot = 0")
    boolean existsByrecipientId(Long userId);

}
