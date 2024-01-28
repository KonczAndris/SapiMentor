package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;

    //@GetMapping("/messages/{senderId}/{recipientId}")
}
