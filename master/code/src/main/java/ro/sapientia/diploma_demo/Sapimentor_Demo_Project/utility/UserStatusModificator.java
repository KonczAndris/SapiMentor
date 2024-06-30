package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.WebSocketController;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@Component
public class UserStatusModificator {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketController webSocketController;


    public void updateUserStatusToOnlineByEmail(String email, int status) {
        Long userId = userRepository.findIdByEmail(email);
        if (userId != null) {
            if (status == 1){
                LocalDateTime online_at = LocalDateTime.now();
                userRepository.updateUserStatusByIdToOnline(userId, status, online_at);
                webSocketController.sendUserStatusUpdate(userId, status, online_at, true);

            }else {
                userRepository.updateUserStatusByIdToOffline(userId, status);
                webSocketController.sendUserStatusUpdate(userId, status, null, false);
            }
        } else {
            throw new RuntimeException("Don't find user with this email address: " + email);
        }
    }

}
