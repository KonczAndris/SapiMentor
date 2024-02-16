package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ChatMessageReadOrNotRepository;

@Component
public class UserProfileNotification {

    private final ChatMessageReadOrNotRepository chatMessageReadOrNotRepository;

    public UserProfileNotification(ChatMessageReadOrNotRepository chatMessageReadOrNotRepository) {
        this.chatMessageReadOrNotRepository = chatMessageReadOrNotRepository;
    }

    @Cacheable("profilenotificationStatus")
    public String getProfileNotificationStatus(Long userId) {
        try {
            if(chatMessageReadOrNotRepository.existsByrecipientId(userId)){
                return "HAVE";
            } else {
                return "NOHAVE";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}
