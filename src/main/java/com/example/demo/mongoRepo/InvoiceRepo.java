package com.example.demo.mongoRepo;

import com.example.demo.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvoiceRepo extends MongoRepository<Invoice, String> {
    Optional<Invoice> findByFvNumber(String fv);
}
