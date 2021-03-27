package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Document
@Data
@Builder
@EqualsAndHashCode
public class Project {
    @Id
    String projectId;

    @NotNull
    private String projectName;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate deadLineDate;
    @NotNull
    private String description;
    @NotNull
    private Set<String> technologies;

    private int peopleNeeded;
    private Boolean ended;

    private Set<Client> employeesOnProject;

}
