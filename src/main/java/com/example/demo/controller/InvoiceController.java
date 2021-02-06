package com.example.demo.controller;

import com.example.demo.elasticRepo.ClientElasticRepo;
import com.example.demo.model.Client;
import com.example.demo.mongoRepo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class InvoiceController {

    private final ClientRepository clientRepository;
    private final ClientElasticRepo clientElasticRepo;

    @Autowired
    public InvoiceController(ClientRepository clientRepository, ClientElasticRepo clientElasticRepo){
        this.clientElasticRepo=clientElasticRepo;
        this.clientRepository=clientRepository;
    }

    @PostMapping("/saveClient")
    public Client saveClient(@RequestBody Client client){
        return clientRepository.save(client);
    }
}
