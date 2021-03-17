package com.example.demo.model;

import com.mongodb.lang.NonNull;
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
    @NonNull
    private String issueTitle;
    @NonNull
    private String issueDetails;
    @NonNull
    private Client reporter;
    @NonNull
    private EIssue status;
    @NonNull
    private String solution;

    private LocalDate assignDate;
    private LocalDate workInProgressDate;
    private LocalDate finishedDate;
}
