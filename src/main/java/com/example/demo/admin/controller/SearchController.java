package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public SearchController(InvoiceRepoElastic invoiceRepoElastic) {
        this.invoiceRepoElastic = invoiceRepoElastic;
    }

    @GetMapping("makeSearch/{phrase}")
        public Set<?> makeSearch(@PathVariable String phrase) {
        String[] phraseArr = phrase.split(" ");
        Set<Invoice> searchResults = new HashSet<>();
        for (String s : phraseArr) {
            searchResults.addAll(invoiceRepoElastic.findNIPWithFuzziness(s));
        }
        return searchResults;
    }
    }

