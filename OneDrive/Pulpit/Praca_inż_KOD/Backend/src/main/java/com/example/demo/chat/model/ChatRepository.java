package com.example.demo.chat.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<ModelChat, String> {
}
