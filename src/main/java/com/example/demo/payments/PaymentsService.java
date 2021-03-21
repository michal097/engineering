package com.example.demo.payments;

import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public List<Invoice> listAllInvoices(int page, int size) {
        var invoicesToPay = invoiceRepo.findAll().stream().filter(i -> !i.isPaid()).collect(Collectors.toList());
        Page<Invoice> invoicesPage = new PageImpl<>(invoicesToPay, PageRequest.of(page, size), invoicesToPay.size());
        return invoicesPage.getContent();

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

    public long getInvoicesToPaySize(){
        return invoiceRepo.findAll().stream().filter(i -> !i.isPaid()).count();
    }


}
