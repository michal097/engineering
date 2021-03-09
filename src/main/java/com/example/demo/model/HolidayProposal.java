package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
@Builder
public class HolidayProposal {
    @Id
    private String holidayProposalId;
    private Client proposalOwner;
    private LocalDate holidayFrom;
    private LocalDate holidayTo;
}
