package com.example.demo.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;


@Document("client")
@Data
@Builder
public class Client {

    @Id
    private String clientId;
    private String username;
    private String name;
    private String surname;
    private String adress;
    private String NIP;
    private String email;
    private Set<String> skills;
    private Set<Invoice> clientInvoices;


}
