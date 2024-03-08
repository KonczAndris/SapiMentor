package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatRoom;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ChatRoomRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // ez a metódus két dolgot csinál
    // 1. megkeresi, hogy van-e már chat room a két user között
    // 2. ha nincs, akkor létrehozza
    // a createNewRoomIfNotExists paraméterrel lehet szabályozni, hogy
    // ha nincs, akkor létrehozza-e
    public Optional<String> getChatRoomId(Long senderId,
                                          Long recipientId,
                                          boolean createNewRoomIfNotExists
                                          ) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId,recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    } else {
                        return Optional.empty();
                    }
                });
    }

private String createChatId(Long senderId, Long recipientId) {
//        var chatId = String.format("%s_%s", senderId, recipientId);
        var chatId = senderId + "_" + recipientId;

        // ez a két objektum azért kell,
        // mert a chat room két irányban is létezik
        // az egyik a sender -> recipient
        // a másik a recipient -> sender
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

}
