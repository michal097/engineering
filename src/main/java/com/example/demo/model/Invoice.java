package com.example.demo.model;

import com.mongodb.lang.NonNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;


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

    private String invoiceDate;

    private String invoiceURL;
    private boolean paid;
    private String clientType;

}
