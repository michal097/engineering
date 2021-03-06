package com.example.demo.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;
import java.util.HashSet;
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
    private Boolean isBusy;
    private Set<Object> projects;

    public void addProject(Project project){
        if(this.projects == null){
            this.projects = new HashSet<>();
        }
        this.projects.add(project);
    }
    public boolean setClientInv(){
        this.setClientInvoices(new HashSet<>());
        return true;
    }


    public boolean setIsBusyInFilter(){
        this.setIsBusy(false);
        return true;
    }

    private LocalDate startProject;
    private LocalDate endProject;

}
