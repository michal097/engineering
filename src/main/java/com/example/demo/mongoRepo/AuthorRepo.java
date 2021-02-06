package com.example.demo.mongoRepo;

import com.example.demo.model.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AuthorRepo extends MongoRepository<Author, String> {
}
