package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    private long invSize;
    private long clientSize;
    private long extClientSize;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchInvoice/{phrase}/{page}/{size}")
    public List<Invoice> makeSearch(@PathVariable String phrase, @PathVariable int page,@PathVariable int size) {

        var searchInv = invoiceRepoElastic.findNIPWithFuzziness(phrase, PageRequest.of(page, size));
        invSize = searchInv.size();

        return searchInv;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchClients/{phrase}/{page}/{size}")
    public List<Client> clients(@PathVariable String phrase, @PathVariable int page, @PathVariable int size) {

         var cl = clientRepository.findClientsBySearchPhrase(phrase, PageRequest.of(page, size));

        clientSize = cl.size();

        return cl;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchExternal/{phrase}/{page}/{size}")
    public List<ExternalClient> extClients(@PathVariable String phrase, @PathVariable int page,@PathVariable int size) {

            var extCl = externalClientRepo.findExternalClientsBySearchPhrase(phrase, PageRequest.of(page, size));

        extClientSize = extCl.size();

        return extCl;
    }


    @GetMapping("invoiceLength")
    public long invoiceSize(){
        return this.invSize;
    }

    @GetMapping("clientLen")
    public long clientSize(){
        return this.clientSize;
    }

    @GetMapping("externalLen")
    public long externalSize(){
        return this.extClientSize;
    }
}

