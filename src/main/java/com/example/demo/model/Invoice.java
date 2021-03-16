package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "invoice")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    @Id
    private String invoiceId;

    private String fvNumber;
    private String bankAccNumber;
    private String invName;
    private String invSurname;
    private String NIP;
    private double costs;
    private String invoiceURL;
    private boolean paid;

}
