package com.example.demo.mongoRepo;

import com.example.demo.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
