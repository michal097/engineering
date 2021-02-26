package com.example.demo.issue.controller;

import com.example.demo.issue.service.IssueService;
import com.example.demo.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService){
        this.issueService=issueService;
    }

    @PostMapping("addIssue")
    public Issue addNewIssue(@RequestBody Issue issue){
        return issueService.addNewIssue(issue);
    }
}
