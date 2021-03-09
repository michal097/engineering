package com.example.demo.model;


import com.example.demo.security.model.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

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
    private Set<HolidayProposal> holidayProposals;

    private Role userType;

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
