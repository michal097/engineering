package com.example.demo.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Document
public class ModelChat {
    @Id
    private String messageId;

    private String message;
    private String username;
    private LocalDate timeStamp;
    private Map<String, List<ModelChat>> personalChat;
    private String modelChatId;
    public ModelChat() {

    }

    public ModelChat(String message) {
        this.message = message;
        this.setTimeStamp(LocalDate.now());
    }

    public ModelChat(String username, String message) {
        this.username = username;
        this.message=message;
    }
}
