package com.example.demo.issue.service;

import com.example.demo.model.EIssue;
import com.example.demo.model.Issue;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.IssueRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class IssueService {

    private final IssueRepo issueRepo;
    private final ClientRepository clientRepository;


    @Autowired
    public IssueService(IssueRepo issueRepo, ClientRepository clientRepository){
        this.issueRepo=issueRepo;
        this.clientRepository=clientRepository;

    }



    public Issue addNewIssue(Issue issue){
        var reporter = SecurityContextHolder.getContext().getAuthentication().getName();
        var client = clientRepository.findAllByUsername(reporter);
        client.ifPresent(issue::setReporter);
        issue.setStatus(EIssue.ASSIGNED);
        issue.setAssignDate(LocalDate.now());
        return issueRepo.save(issue);
    }

    public List<Issue> listAllIssues(){
        return issueRepo.findAll();
    }
    public Issue getIssue(String issueId) throws Exception{
        return issueRepo.findById(issueId).orElseThrow(Exception::new);
    }

    public Issue makeWorkInProgress(Issue issue){
        return issueRepo.findById(issue.getIssueId()).map(i -> {
           i.setStatus(EIssue.WORK_IN_PROGRESS);
            issue.setWorkInProgressDate(LocalDate.now());
           return issueRepo.save(i);
        }).orElse(null);
    }

    public Issue saveIssueSolution(Issue issue){
        return issueRepo.findById(issue.getIssueId()).map(i ->{
            i.setSolution(issue.getSolution());
            i.setStatus(EIssue.FINISHED);
            issue.setFinishedDate(LocalDate.now());
            return issueRepo.save(i);
        }).orElse(null);
    }
}
