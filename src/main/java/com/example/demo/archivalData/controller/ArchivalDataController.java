package com.example.demo.archivalData.controller;

import com.example.demo.archivalData.service.ArchivalService;
import com.example.demo.model.Invoice;
import com.example.demo.model.Issue;
import com.example.demo.model.Project;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin
public class ArchivalDataController {

    private final ArchivalService archivalService;

    public ArchivalDataController(ArchivalService archivalService) {
        this.archivalService = archivalService;
    }

    @GetMapping("archivalInvoices")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public Set<Invoice> getArchInvoices() {
        return archivalService.collectArchivalInvoices();
    }


    @GetMapping("archivalIssue")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public Set<Issue> getArchIssues() {
        return archivalService.collectArchivalIssues();
    }

    @GetMapping("archivalProjects")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public Set<Project> getArchProjects() {
        return archivalService.collectArchivalProjects();
    }
}
