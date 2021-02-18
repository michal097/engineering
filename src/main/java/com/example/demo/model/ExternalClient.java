package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("externalClient")
@Data
@Builder
public class ExternalClient {
    @Id
    String externalClientId;
    private String name;
    private String surname;
    private String nip;
    private String bankAccNumber;

    private Set<Invoice> externalClientInvoices;

}
