package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class SearchController {


    private final InvoiceRepoElastic invoiceRepoElastic;
    private final ClientRepoElastic clientRepository;
    private final ExtClientRepoElastic externalClientRepo;
    private long invSize;
    private long clientSize;
    private long extClientSize;
    @Autowired
    public SearchController(InvoiceRepoElastic invoiceRepoElastic, ClientRepoElastic clientRepository, ExtClientRepoElastic externalClientRepo) {
        this.invoiceRepoElastic = invoiceRepoElastic;
        this.clientRepository = clientRepository;
        this.externalClientRepo = externalClientRepo;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchInvoice/{phrase}/{page}/{size}")
    public List<Invoice> makeSearch(@PathVariable String phrase, @PathVariable int page, @PathVariable int size) {

        List<Invoice> searchInv = invoiceRepoElastic.findNIPWithFuzziness(phrase, PageRequest.of(page, size));

        invSize = searchInv.size();
        log.info("found {} record for invoice search", invSize);

        return searchInv;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchClients/{phrase}/{page}/{size}")
    public List<Client> clients(@PathVariable String phrase, @PathVariable int page, @PathVariable int size) {

        var cl = clientRepository.findClientsBySearchPhrase(phrase, PageRequest.of(page, size));

        clientSize = cl.size();
        log.info("found {} record for client search", clientSize);

        return cl;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("searchExternal/{phrase}/{page}/{size}")
    public List<ExternalClient> extClients(@PathVariable String phrase, @PathVariable int page, @PathVariable int size) {

        var extCl = externalClientRepo.findExternalClientsBySearchPhrase(phrase, PageRequest.of(page, size));

        extClientSize = extCl.size();
        log.info("found {} record for external client search", extClientSize);

        return extCl;
    }


    @GetMapping("invoiceLength")
    public long invoiceSize() {
        return this.invSize;
    }

    @GetMapping("clientLen")
    public long clientSize() {
        return this.clientSize;
    }

    @GetMapping("externalLen")
    public long externalSize() {
        return this.extClientSize;
    }
}

