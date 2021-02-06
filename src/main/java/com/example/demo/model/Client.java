package com.example.demo.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "client")
@Data
public class Client {

    @Id
    private String clientId;

    private String name;
    private String surname;
    private String adress;
    private String NIP;
    private String price;



}
