package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
// ez a bulider pattern miatt kell
// ami azt jelenti, hogy segít a konstruktorok létrehozásában
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
}
