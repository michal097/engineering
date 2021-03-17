package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Builder
@Data
public class Issue {
    @Id
    String issueId;

    private String issueTitle;
    private String issueDetails;
    private Client reporter;
    private EIssue status;
    private String solution;
    private LocalDate assignDate;
    private LocalDate workInProgressDate;
    private LocalDate finishedDate;
}
