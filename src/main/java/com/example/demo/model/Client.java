package com.example.demo.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.util.Set;


@Document(indexName = "client")
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
    @Getter@Setter
    private Boolean isBusy;

   // private Set<Project> projects;

    public void addProject(Project project){
        Set.of(project);
    }

    private LocalDate startProject;
    private LocalDate endProject;





}
