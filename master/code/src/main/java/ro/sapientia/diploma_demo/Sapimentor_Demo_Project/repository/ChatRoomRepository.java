package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatRoom;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    @Modifying
    @Query("DELETE FROM ChatRoom c WHERE c.senderId = :userId OR c.recipientId = :userId")
    void deleteBySenderIdOrRecipientId(Long userId);
}
