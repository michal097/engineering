package com.example.demo.issue.service;

import com.example.demo.mail.MailService;
import com.example.demo.model.EIssue;
import com.example.demo.model.Issue;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.IssueRepo;
import com.example.demo.security.model.Role;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class IssueService {

    private final IssueRepo issueRepo;
    private final ClientRepository clientRepository;
    private final MailService mailService;

    @Autowired
    public IssueService(IssueRepo issueRepo, ClientRepository clientRepository, MailService mailService) {
        this.issueRepo = issueRepo;
        this.clientRepository = clientRepository;
        this.mailService = mailService;

    }

    public Issue addNewIssue(Issue issue) {
        String reporter = SecurityContextHolder.getContext().getAuthentication().getName();
        var client = clientRepository.findAllByUsername(reporter);
        client.ifPresent(issue::setReporter);
        issue.setStatus(EIssue.ASSIGNED);
        issue.setAssignDate(LocalDate.now());

        var findModerator = clientRepository.findAll().stream().filter(m -> m.getUserType() != null && m.getUserType().equals(Role.ROLE_MODERATOR)).findAny();

        findModerator.ifPresent(moder -> mailService.sendSimpleMail(moder.getEmail(), "Received new issue: " + issue.getIssueTitle(), issue.getIssueDetails()));

        return issueRepo.save(issue);
    }

    public List<Issue> listAllIssues() {
        String reporter = SecurityContextHolder.getContext().getAuthentication().getName();
        var getUser = clientRepository.findAllByUsername(reporter);
        var isUser = false;
        if (getUser.isPresent()) {
            isUser = getUser.get().getUserType().equals(Role.ROLE_USER);
            if (isUser) {
                return issueRepo.findAll().stream().filter(rep -> rep.getReporter().equals(getUser.get())).collect(Collectors.toList());
            }
        }

        return issueRepo.findAll().stream().filter(issue -> !issue.getStatus().equals(EIssue.FINISHED)).collect(Collectors.toList());
    }

    public Issue getIssue(String issueId) throws Exception {
        return issueRepo.findById(issueId).orElseThrow(Exception::new);
    }

    public Issue makeWorkInProgress(Issue issue) {
        return issueRepo.findById(issue.getIssueId()).map(i -> {
            i.setStatus(EIssue.WORK_IN_PROGRESS);
            issue.setWorkInProgressDate(LocalDate.now());
            return issueRepo.save(i);
        }).orElse(null);
    }

    public Issue saveIssueSolution(Issue issue) {
        return issueRepo.findById(issue.getIssueId()).map(i -> {
            i.setSolution(issue.getSolution());
            i.setStatus(EIssue.FINISHED);
            issue.setFinishedDate(LocalDate.now());

            var issueOwnerEmail = issue.getReporter().getEmail();
            mailService.sendSimpleMail(issueOwnerEmail,
                    issue.getIssueTitle() + " has been solved",
                    issue.getIssueDetails() +
                            '\n' + "solution: " + issue.getSolution());

            return issueRepo.save(i);
        }).orElse(null);
    }
}
