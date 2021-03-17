package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
public class SearchController {


    private final InvoiceRepoElastic invoiceRepoElastic;
    private final ClientRepoElastic clientRepository;
    private final ExtClientRepoElastic externalClientRepo;

    @Autowired
    public SearchController(InvoiceRepoElastic invoiceRepoElastic, ClientRepoElastic clientRepository, ExtClientRepoElastic externalClientRepo) {
        this.invoiceRepoElastic = invoiceRepoElastic;
        this.clientRepository = clientRepository;
        this.externalClientRepo = externalClientRepo;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchInvoice/{phrase}")
    public Set<?> makeSearch(@PathVariable String phrase) {
        String[] phraseArr = phrase.split(" ");
        Set<Invoice> searchResults = new HashSet<>();
        for (String s : phraseArr) {
            searchResults.addAll(invoiceRepoElastic.findNIPWithFuzziness(s));
        }
        return searchResults;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchClients/{phrase}")
    public Set<Client> clients(@PathVariable String phrase) {
        String[] phraseArr = phrase.split(" ");
        Set<Client> cl = new HashSet<>();
        for (String c : phraseArr) {
            cl.addAll(clientRepository.findClientsBySearchPhrase(c));
        }
        return cl;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchExternal/{phrase}")
    public Set<ExternalClient> extClients(@PathVariable String phrase) {
        String[] phraseArr = phrase.split(" ");
        Set<ExternalClient> extCl = new HashSet<>();
        for (String c : phraseArr) {
            extCl.addAll(externalClientRepo.findExternalClientsBySearchPhrase(c));
        }
        return extCl;
    }
}

