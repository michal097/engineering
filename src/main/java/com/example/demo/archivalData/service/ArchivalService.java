package com.example.demo.archivalData.service;

import com.example.demo.model.EIssue;
import com.example.demo.model.Invoice;
import com.example.demo.model.Issue;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.mongoRepo.IssueRepo;
import com.example.demo.mongoRepo.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class ArchivalService {

    private final InvoiceRepo invoiceRepo;
    private final IssueRepo issueRepo;
    private final ProjectRepository projectRepository;

    @Autowired
    public ArchivalService(InvoiceRepo invoiceRepo, IssueRepo issueRepo, ProjectRepository projectRepository){
        this.issueRepo=issueRepo;
        this.invoiceRepo=invoiceRepo;
        this.projectRepository=projectRepository;
    }

    public Set<Invoice> collectArchivalInvoices(){
        return invoiceRepo.findAll().stream().filter(Invoice::isPaid).collect(toSet());
    }
    public Set<Issue> collectArchivalIssues(){
        return issueRepo.findAll().stream().filter(i->i.getStatus().equals(EIssue.FINISHED)).collect(toSet());
    }
    public Set<Project> collectArchivalProjects(){
        return projectRepository.findAll().stream().filter(Project::getEnded).collect(toSet());
    }


}
