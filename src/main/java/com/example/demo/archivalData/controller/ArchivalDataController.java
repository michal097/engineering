package com.example.demo.archivalData.controller;

import com.example.demo.archivalData.service.ArchivalService;
import com.example.demo.model.Invoice;
import com.example.demo.model.Issue;
import com.example.demo.model.Project;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ArchivalDataController {

    private final ArchivalService archivalService;

    public ArchivalDataController(ArchivalService archivalService) {
        this.archivalService = archivalService;
    }

    @GetMapping("archivalInvoices/{page}/{size}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public List<Invoice> getArchInvoices(@PathVariable int page, @PathVariable int size) {
        return archivalService.collectArchivalInvoices(page, size);
    }


    @GetMapping("archivalIssue/{page}/{size}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public List<Issue> getArchIssues(@PathVariable int page, @PathVariable int size) {
        return archivalService.collectArchivalIssues(page, size);
    }

    @GetMapping("archivalProjects/{page}/{size}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")

    public List<Project> getArchProjects(@PathVariable int page, @PathVariable int size) {
        return archivalService.collectArchivalProjects(page, size);
    }

    @GetMapping("getInvoiceSize")
    public long getInvoiceSize(){
        return archivalService.getInvoiceSize();
    }
    @GetMapping("getIssuesSize")
    public long getIssuesSize(){
        return archivalService.getIssuesSize();
    }
    @GetMapping("getProjectSize")
    public long getProjectSize(){
        return archivalService.getProjectSize();
    }


}
