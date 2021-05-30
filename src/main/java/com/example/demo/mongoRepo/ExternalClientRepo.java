package com.example.demo.mongoRepo;

import com.example.demo.model.ExternalClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ExternalClientRepo extends MongoRepository<ExternalClient, String> {
    Optional<ExternalClient> findByNip(String nip);
    Page<ExternalClient> findAll(Pageable pageable);

}
