package com.example.demo.model;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Size;
import java.util.Set;

@Document(indexName = "external")
@Data
@Builder
public class ExternalClient {
    @Id
    private String externalClientId;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    @Size(min = 10, max = 10)
    private String nip;
    @NonNull
    private String bankAccNumber;
    private double costs;

    private Set<Invoice> externalClientInvoices;

}
