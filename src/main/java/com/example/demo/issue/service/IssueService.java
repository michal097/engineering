package com.example.demo.issue.service;

import com.example.demo.model.EIssue;
import com.example.demo.model.Issue;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.IssueRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return issueRepo.save(issue);
    }
    public List<Issue> listAllIssues(){
        return issueRepo.findAll();
    }
    public Issue getIssue(String issueId) throws Exception{
        return issueRepo.findById(issueId).orElseThrow(Exception::new);
    }
}
