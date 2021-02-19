package com.example.demo.mongoRepo;

import com.example.demo.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findAllByUsername(String username);

    Optional<Client> findByNIP(String nip);

    Optional<Client> findByUsername(String username);
}
