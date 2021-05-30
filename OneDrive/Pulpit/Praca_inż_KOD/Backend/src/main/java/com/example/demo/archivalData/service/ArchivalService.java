package com.example.demo.archivalData.service;

import com.example.demo.model.EIssue;
import com.example.demo.model.Invoice;
import com.example.demo.model.Issue;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.mongoRepo.IssueRepo;
import com.example.demo.mongoRepo.ProjectRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ArchivalService {

    private final InvoiceRepo invoiceRepo;
    private final IssueRepo issueRepo;
    private final ProjectRepository projectRepository;

    @Autowired
    public ArchivalService(InvoiceRepo invoiceRepo, IssueRepo issueRepo, ProjectRepository projectRepository) {
        this.issueRepo = issueRepo;
        this.invoiceRepo = invoiceRepo;
        this.projectRepository = projectRepository;
    }

    public List<Invoice> collectArchivalInvoices(int page, int size) {
        var inv = invoiceRepo.findAll().stream().filter(Invoice::isPaid).collect(toList());
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> i = new PageImpl<>(inv, pageable, inv.size());
        return i.getContent();
    }


    public List<Issue> collectArchivalIssues(int page, int size) {
        var iss = issueRepo.findAll().stream().filter(i -> i.getStatus().equals(EIssue.FINISHED)).collect(toList());
        Pageable pageable = PageRequest.of(page, size);
        Page<Issue> i = new PageImpl<>(iss, pageable, iss.size());
        return i.getContent();
    }

    public List<Project> collectArchivalProjects(int page, int size) {
        var p = projectRepository.findAll().stream().filter(Project::getEnded).collect(toList());
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> pro = new PageImpl<>(p, pageable, p.size());
        return pro.getContent();
    }

    public long getInvoiceSize() {
        return invoiceRepo.findAll().stream().filter(Invoice::isPaid).count();
    }

    public long getIssuesSize() {
        return issueRepo.findAll().stream().filter(i -> i.getStatus().equals(EIssue.FINISHED)).count();
    }

    public long getProjectSize() {
        return projectRepository.findAll().stream().filter(Project::getEnded).count();
    }
}
