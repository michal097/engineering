package com.example.demo.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class PersonalChatPokes {
    @Id
    private String personalChatPokesId;

    private String chatID;
    private String sendTo;
    private String sendFrom;
    private int counter;
    private LocalDateTime timeStamp;
    public PersonalChatPokes() {

    }

    public PersonalChatPokes(String chatID, String sendTo, String sendFrom, int counter) {
        this.chatID = chatID;
        this.sendTo = sendTo;
        this.sendFrom = sendFrom;
        this.counter = counter;
    }

}
