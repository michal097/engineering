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
    private final WiseTransfer wiseTransfer;
    @Autowired
    PaymentController(PaymentsService paymentsService, WiseTransfer wiseTransfer) {
        this.paymentsService = paymentsService;
        this.wiseTransfer=wiseTransfer;

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("pay/{amount}/{name}/{iban}/{invoiceId}")
        public Invoice str(@PathVariable double amount, @PathVariable String name, @PathVariable String iban, @PathVariable String invoiceId){
        String targetCurr;
        if(iban.startsWith("PL")){
            targetCurr = "PLN";
        }else targetCurr = "EUR";
        if( wiseTransfer.fundTransfer(amount, targetCurr, name, iban) .equals("COMPLETED")){
            return paymentsService.changePaymentStatus(invoiceId);
        }else throw new IllegalArgumentException();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getAllIvoices/{page}/{size}")
    public List<Invoice> allInvoices(@PathVariable int page, @PathVariable int size) {
        return paymentsService.listAllInvoices(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getSpecInvoiceById/{id}")
    public Invoice getinvoiceById(@PathVariable String id) {
        return paymentsService.getInvoice(id);
    }
    @GetMapping("getInvoiceIdByUUID/{UUID}")
    public String getIdByUUIDInvoice(@PathVariable String UUID){
        return paymentsService.getInvoiceIdByUUID(UUID);
    }
    @GetMapping("getInvoicesToPaySize")
    public long getInvoicesSize(){
        return paymentsService.getInvoicesToPaySize();
    }
}
