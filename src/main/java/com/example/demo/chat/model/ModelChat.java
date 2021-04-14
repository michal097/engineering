package com.example.demo.chat.model;

import lombok.Data;

@Data
public class ModelChat {

    private String message;
    private String username;
    public ModelChat(){

    }
    public ModelChat(String message) {
        this.message = message;
    }

}
