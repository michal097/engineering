package com.example.demo.issue.service;

import com.example.demo.model.EIssue;
import com.example.demo.model.Issue;
import com.example.demo.model.IssueCounter;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.IssueCounterRepo;
import com.example.demo.mongoRepo.IssueRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Service
public class IssueService {

    private final IssueRepo issueRepo;
    private final ClientRepository clientRepository;
    private final IssueCounterRepo issueCounterRepo;

    @Autowired
    public IssueService(IssueRepo issueRepo, ClientRepository clientRepository, IssueCounterRepo issueCounterRepo){
        this.issueRepo=issueRepo;
        this.clientRepository=clientRepository;
        this.issueCounterRepo = issueCounterRepo;
    }

    private int counter;

    public Issue addNewIssue(Issue issue){
        var reporter = SecurityContextHolder.getContext().getAuthentication().getName();
        var client = clientRepository.findAllByUsername(reporter);
        client.ifPresent(issue::setReporter);
        issue.setStatus(EIssue.ASSIGNED);
        this.getCountNewIssues(1);
        issue.setAssignDate(LocalDate.now());
        return issueRepo.save(issue);
    }
    //todo
    public Stream<IssueCounter> getCountNewIssues(int count){
        if(issueCounterRepo.findAll().size()==0){
            issueCounterRepo.save(new IssueCounter());
        }
        return issueCounterRepo.findAll().stream().map(issueCounter -> {
            issueCounter.setCounter(issueCounter.getCounter() + count);
            return issueCounterRepo.save(issueCounter);
        });
    }//todo
    public int getCount(){
        return issueCounterRepo.findAll().get(0).getCounter();
    }//todo
    public void flushCountNewIssue(){
        issueCounterRepo.findAll().stream().map(i->{
            i.setCounter(0);
            return issueCounterRepo.save(i);
        });
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
