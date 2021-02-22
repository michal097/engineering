package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Set;

@Document
@Data
@Builder
public class Project {
    @Id String projectId;

    private String projectName;
    private LocalDate startDate;
    private LocalDate deadLineDate;
    private String description;
    private Set<String> technologies;

    @DBRef
    private Set<Client> employeesOnProject;
}
