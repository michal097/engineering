package com.example.demo.archivalData.controller;

import com.example.demo.archivalData.service.ArchivalService;
import com.example.demo.model.Invoice;
import com.example.demo.model.Issue;
import com.example.demo.model.Project;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin
public class ArchivalDataController {

    private final ArchivalService archivalService;

    public ArchivalDataController(ArchivalService archivalService){
        this.archivalService=archivalService;
    }

    @GetMapping("archivalInvoices")
    public Set<Invoice> getArchInvoices(){
        return archivalService.collectArchivalInvoices();
    }


    @GetMapping("archivalIssue")
    public Set<Issue> getArchIssues(){
        return archivalService.collectArchivalIssues();
    }

    @GetMapping("archivalProjects")
    public Set<Project> getArchProjects(){
        return archivalService.collectArchivalProjects();
    }
}
