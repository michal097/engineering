package com.example.demo.mongoRepo;

import com.example.demo.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepo extends MongoRepository<Invoice, String> {

}
