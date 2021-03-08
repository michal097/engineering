package com.example.demo.issue.controller;

import com.example.demo.issue.service.IssueService;
import com.example.demo.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("allIssues")
    public List<Issue> allIssues(){
        return issueService.listAllIssues();
    }
    @GetMapping("getIssue/{id}")
    public Issue getIssue(@PathVariable String id) throws Exception{
        return issueService.getIssue(id);
    }
    @PostMapping("makeWorkInProgress")
    public Issue makeIssueWorkInProgress(@RequestBody Issue issue){

        return issueService.makeWorkInProgress(issue);
    }
    @PostMapping("saveIssueSolution")
    public Issue saveIssueSolution(@RequestBody Issue issue){
        return issueService.saveIssueSolution(issue);
    }
}
