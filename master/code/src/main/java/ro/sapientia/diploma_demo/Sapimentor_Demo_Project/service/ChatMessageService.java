package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessage;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessageReadOrNot;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ChatMessageReadOrNotRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ChatMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageReadOrNotRepository chatMessageReadOrNotRepository;

    // itt elmentjuk az uzenetet
    // a chatId-t a chatRoomService segitsegevel kapjuk meg
    // a chatMessageRepository segitsegevel mentjuk el az uzenetet
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(),
                        chatMessage.getRecipientId(),
                        true
                ).orElseThrow();
        chatMessage.setChatId(chatId);

        // create chatMessageReadOrNot when save chatMessage and not exist chatMessageReadOrNot
        //ChatMessageReadOrNot chatMessageReadOrNot = new ChatMessageReadOrNot();

        ChatMessageReadOrNot chatMessageReadOrNot = chatMessageReadOrNotRepository.findByChatIdAndSenderId(chatId, chatMessage.getSenderId());
        System.out.println("ChatMessageReadOrNotIgen: " + chatMessageReadOrNot);
        if (chatMessageReadOrNot == null) {
            chatMessageReadOrNot = new ChatMessageReadOrNot();
            chatMessageReadOrNot.setChatId(chatId);
            chatMessageReadOrNot.setSenderId(chatMessage.getSenderId());
            chatMessageReadOrNot.setRecipientId(chatMessage.getRecipientId());
            chatMessageReadOrNot.setReadOrNot(0);
            chatMessageReadOrNotRepository.save(chatMessageReadOrNot);
            System.out.println("ChatMessageReadOrNot not exist");
        } else {
            chatMessageReadOrNot.setReadOrNot(0);
            chatMessageReadOrNotRepository.save(chatMessageReadOrNot);
            System.out.println("ChatMessageReadOrNot already exist");
        }

        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    // itt lekerjuk az osszes uzenetet
    // a chatId-t a chatRoomService segitsegevel kapjuk meg
    // a chatMessageRepository segitsegevel kerjuk le az osszes uzenetet
    public List<ChatMessage> findChatMessages(Long senderId,
                                              Long recipientId) {
        var chatId = chatRoomService.
                getChatRoomId(senderId,
                        recipientId,
                        false);
        return chatId.map(chatMessageRepository::findByChatId)
                .orElse(new ArrayList<>());
    }


}
