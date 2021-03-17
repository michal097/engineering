package com.example.demo.payments;

import com.example.demo.model.Client;
import com.example.demo.model.Invoice;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Data
public class Payment {
    private @Id
    String paymentId;
    private Double amount;
    private Client client;
    private Invoice referencedInvoice;
}
