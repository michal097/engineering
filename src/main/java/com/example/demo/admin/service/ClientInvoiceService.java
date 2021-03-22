package com.example.demo.admin.service;

import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ExternalClientRepo;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientInvoiceService {


    private final ClientRepository clientRepository;
    private final InvoiceRepo invoiceRepo;
    private final ExternalClientRepo externalClientRepo;

    @Autowired
    public ClientInvoiceService(ClientRepository clientRepository, InvoiceRepo invoiceRepo, ExternalClientRepo externalClientRepo) {
        this.clientRepository = clientRepository;
        this.invoiceRepo = invoiceRepo;
        this.externalClientRepo = externalClientRepo;
    }

    public void checkClient(Invoice invoice) {
        Optional<Client> clientIsPresent =
                clientRepository.findByNIP(invoice.getNIP());

        Optional<ExternalClient> externalClient =
                externalClientRepo.findByNip(invoice.getNIP());


        if (clientIsPresent.isPresent()) {
            var client = clientIsPresent.get();
            if (client.getClientInvoices() == null) {
                client.setClientInvoices(new HashSet<>());
            }
            client.getClientInvoices().add(invoice);

            clientRepository.save(client);
        } else if (externalClient.isPresent()) {
            var extClient = externalClient.get();
            if (extClient.getExternalClientInvoices() == null) {
                Set<Invoice> inv = new HashSet<>();
                inv.add(invoice);
                extClient.setExternalClientInvoices(inv);
            } else {
                extClient.getExternalClientInvoices().add(invoice);
            }
            externalClientRepo.save(extClient);
        } else {
            var newExternalClient = ExternalClient.builder()
                    .name(invoice.getInvName())
                    .surname(invoice.getInvSurname())
                    .nip(invoice.getNIP())
                    .bankAccNumber(invoice.getBankAccNumber())
                    .externalClientInvoices(Set.of(invoice))
                    .build();
            externalClientRepo.save(newExternalClient);
        }

    }

    public void saveInvoice(Invoice invoice) {

        Optional<Invoice> isAlreadyInRepo = invoiceRepo.findByFvNumber(invoice.getFvNumber());

        if (!isAlreadyInRepo.isPresent()) {
            invoiceRepo.save(invoice);
        }
    }
}
