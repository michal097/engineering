package com.example.demo.admin.service;

import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ExternalClientRepo;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class ClientInvoiceService {


    private final ClientRepository clientRepository;
    private final InvoiceRepo invoiceRepo;
    private final ExternalClientRepo externalClientRepo;
    private final ExtClientRepoElastic extClientRepoElastic;
    private Invoice invoiceClient;

    @Autowired
    public ClientInvoiceService(ClientRepository clientRepository, ExtClientRepoElastic extClientRepoElastic, InvoiceRepo invoiceRepo, ExternalClientRepo externalClientRepo) {
        this.clientRepository = clientRepository;
        this.invoiceRepo = invoiceRepo;
        this.externalClientRepo = externalClientRepo;
        this.extClientRepoElastic = extClientRepoElastic;
    }

    public void checkClient(Invoice invoice) {
        Optional<Client> clientIsPresent =
                clientRepository.findByNIP(invoice.getNIP());

        Optional<ExternalClient> externalClient =
                externalClientRepo.findByNip(invoice.getNIP());


        if (clientIsPresent.isPresent()) {
            invoiceClient = invoice;
            var client = clientIsPresent.get();
            if (client.getClientInvoices() == null) {
                client.setClientInvoices(new HashSet<>());
            }
            invoiceClient.setClientType("Internal");
            client.getClientInvoices().add(invoiceClient);

            clientRepository.save(client);
        } else if (externalClient.isPresent()) {
            invoiceClient = invoice;
            invoiceClient.setClientType("External");
            invoiceClient.setInvoiceId(UUID.randomUUID().toString());
            var extClient = externalClient.get();
            if (extClient.getExternalClientInvoices() == null) {
                Set<Invoice> inv = new HashSet<>();
                inv.add(invoiceClient);
                extClient.setExternalClientInvoices(inv);

            } else {
                invoiceClient.setClientType("External");
                invoiceClient.setInvoiceId(UUID.randomUUID().toString());
                double costs = extClient.getCosts() + invoice.getCosts();
                extClient.setCosts(costs);
                extClient.getExternalClientInvoices().add(invoiceClient);
                extClientRepoElastic.findByNip(extClient.getNip()).ifPresent(e -> {
                    e.setCosts(costs);
                    extClientRepoElastic.save(e);
                });
            }
            externalClientRepo.save(extClient);
        } else {
            invoice.setClientType("External");
            var newExternalClient = ExternalClient.builder()
                    .name(invoice.getInvName())
                    .surname(invoice.getInvSurname())
                    .nip(invoice.getNIP())
                    .bankAccNumber(invoice.getBankAccNumber())
                    .externalClientInvoices(Set.of(invoice))
                    .costs(invoice.getCosts())
                    .build();
            externalClientRepo.save(newExternalClient);
        }

    }

    public void saveInvoice(Invoice invoice) {
        invoiceClient = invoice;
        Optional<Invoice> isAlreadyInRepo = invoiceRepo.findByFvNumber(invoiceClient.getFvNumber());

        if (!isAlreadyInRepo.isPresent()) {
            invoiceRepo.save(invoiceClient);
        }
    }
}
