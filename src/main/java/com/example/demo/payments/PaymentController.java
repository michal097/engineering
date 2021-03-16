package com.example.demo.payments;

import com.example.demo.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
public class PaymentController {

    private final StripeClient stripeClient;
    private final PaymentsService paymentsService;

    @Autowired
    PaymentController(StripeClient stripeClient, PaymentsService paymentsService) {
        this.paymentsService=paymentsService;
        this.stripeClient = stripeClient;
    }

    @GetMapping("/charge/{IBAN}/{amount}")
    public String chargeCard(@PathVariable String IBAN, @PathVariable Long amount) throws Exception {
        return stripeClient.makePayout().toJson();
    }

    @GetMapping("getAllIvoices")
    public List<Invoice> allInvoices(){
        return paymentsService.listAllInvoices();
    }
    @GetMapping("getSpecInvoiceById/{id}")
    public Invoice getinvoiceById(@PathVariable String id){
        System.out.println(paymentsService.getInvoice(id));
        return paymentsService.getInvoice(id);
    }

    @PostMapping("makePayment")
    public Invoice makePayment(@RequestBody Invoice invoice){
        return paymentsService.changePaymentStatus(invoice);
    }
}
