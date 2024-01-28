package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
}
