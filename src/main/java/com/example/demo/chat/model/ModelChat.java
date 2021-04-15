package com.example.demo.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
public class ModelChat {
    @Id
    private String messageId;

    private String message;
    private String username;
    private LocalDate timeStamp;

    public ModelChat() {

    }

    public ModelChat(String message) {
        this.message = message;
        this.setTimeStamp(LocalDate.now());
    }

}
