package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessage;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ChatMessageService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.ChatNotification;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    //ez a simpMessagingTemplate segitsegevel kuldjuk el az uzeneteket
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);

        simpMessagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(),
                "/queue/messages",
                ChatNotification.builder()
                        .chatId(savedMsg.getId())
                        .senderId(savedMsg.getSenderId())
                        .recipientId(savedMsg.getRecipientId())
                        .content(savedMsg.getContent())
                        .build()
        );
    }

    // itt lekerjuk az osszes uzenetet
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable Long senderId,
            @PathVariable Long recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
