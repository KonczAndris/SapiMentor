package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendUserStatusUpdate(Long userId, int status, LocalDateTime online_at, boolean signed_in) {
        Map<String, Object> userStatusUpdate = new HashMap<>();
        userStatusUpdate.put("userId", userId);
        userStatusUpdate.put("status", status);
        userStatusUpdate.put("online_at", online_at);
        userStatusUpdate.put("signed_in", signed_in);
        messagingTemplate.convertAndSend("/user/public/userStatusUpdate", userStatusUpdate);
    }


}
