package com.example.demo.model;

import com.mongodb.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@Document(indexName = "invoice")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

    @Id
    private String invoiceId;

    @NonNull
    private String fvNumber;
    @NonNull
    private String bankAccNumber;
    @NonNull
    private String invName;
    @NonNull
    private String invSurname;
    @NonNull
    @Size(min = 10, max = 10)
    private String NIP;
    @Min(1)
    private double costs;

    private String invoiceURL;
    private boolean paid;

}
