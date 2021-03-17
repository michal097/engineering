package com.example.demo.payments;

import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.ExcerptProjector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentsService {

    private final InvoiceRepo invoiceRepo;

    @Autowired
    public PaymentsService(InvoiceRepo invoiceRepo) {
        this.invoiceRepo = invoiceRepo;
    }

    public List<Invoice> listAllInvoices() {
        return invoiceRepo.findAll().stream().filter(i -> !i.isPaid()).collect(Collectors.toList());
    }

    public Invoice getInvoice(String id) {
        return invoiceRepo.findById(id).orElse(null);
    }

    public Invoice changePaymentStatus(Invoice invoice) {
        invoiceRepo.findById(invoice.getInvoiceId()).map(i -> {
            i.setPaid(true);
            return invoiceRepo.save(i);
        });
        return null;
    }

}
