package com.example.demo.model;

import com.example.demo.security.model.Role;
import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Document(indexName = "client")
@Data
@Builder
@EqualsAndHashCode
public class Client {

    @Id
    private String clientId;
    private String username;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String adress;
    @NonNull
    @Size(min = 10, max = 10)
    private String NIP;
    @NonNull
    @Email
    private String email;

    private Set<String> skills;
    private Set<Invoice> clientInvoices;
    private Boolean isBusy;
    private Set<Object> projects;
    private Role userType;
    private String actualProject;

    //chat
    private Set<String> chatIds;

    public void addProject(Project project) {
        if (this.projects == null) {
            this.projects = new HashSet<>();
        }
        this.projects.add(project);
    }

    public boolean setIsBusyInFilter() {
        this.setIsBusy(false);
        return true;
    }

    private LocalDate startProject;
    private LocalDate endProject;

}
