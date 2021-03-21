package com.example.demo.payments;

import com.example.demo.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class PaymentController {

    private final PaymentsService paymentsService;

    @Autowired
    PaymentController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;

    }



    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getAllIvoices/{page}/{size}")
    public List<Invoice> allInvoices(@PathVariable int page, @PathVariable int size) {
        return paymentsService.listAllInvoices(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getSpecInvoiceById/{id}")
    public Invoice getinvoiceById(@PathVariable String id) {
        System.out.println(paymentsService.getInvoice(id));
        return paymentsService.getInvoice(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("makePayment")
    public Invoice makePayment(@RequestBody Invoice invoice) {
        return paymentsService.changePaymentStatus(invoice);
    }

    @GetMapping("getInvoicesToPaySize")
    public long getInvoicesSize(){
        return paymentsService.getInvoicesToPaySize();
    }
}
