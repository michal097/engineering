package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


import java.util.Set;

@Document(indexName = "externalclient")
@Data
@Builder
public class ExternalClient {
    @Id
    private String externalClientId;
    private String name;
    private String surname;
    private String nip;
    private String bankAccNumber;

    private Set<Invoice> externalClientInvoices;

}
