package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendUserStatusUpdate(Long userId, int status) {
        Map<String, Object> userStatusUpdate = new HashMap<>();
        userStatusUpdate.put("userId", userId);
        userStatusUpdate.put("status", status);
        messagingTemplate.convertAndSend("/topic/userStatusUpdate", userStatusUpdate);
    }


}