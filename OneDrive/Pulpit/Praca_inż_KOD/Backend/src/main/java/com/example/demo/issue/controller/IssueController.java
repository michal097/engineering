package com.example.demo.issue.controller;

import com.example.demo.issue.service.IssueService;
import com.example.demo.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")

    @PostMapping("addIssue")
    public Issue addNewIssue(@RequestBody Issue issue) {
        return issueService.addNewIssue(issue);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR','ROLE_USER')")
    @GetMapping("allIssues/{page}/{size}")
    public List<Issue> allIssues(@PathVariable int page, @PathVariable int size) {
        return issueService.listAllIssues(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MODERATOR')")
    @GetMapping("getIssue/{id}")
    public Issue getIssue(@PathVariable String id) throws Exception {
        return issueService.getIssue(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @PostMapping("makeWorkInProgress")
    public Issue makeIssueWorkInProgress(@RequestBody Issue issue) {

        return issueService.makeWorkInProgress(issue);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    @PostMapping("saveIssueSolution")
    public Issue saveIssueSolution(@RequestBody Issue issue) {
        return issueService.saveIssueSolution(issue);
    }

    @GetMapping("issuesLen")
    public long notEndedIssues() {
        return issueService.countNotEndedIssues();
    }
}
