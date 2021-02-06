package com.example.demo.security.repository;


import com.example.demo.security.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    void deleteByUsername(String username);
}
