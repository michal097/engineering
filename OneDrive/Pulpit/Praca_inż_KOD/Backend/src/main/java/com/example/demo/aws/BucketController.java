package com.example.demo.aws;


import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.ClientInvoiceService;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
public class BucketController {
    private final InvoiceRepo invoiceRepo;
    private final AmazonClient amazonClient;
    private final AdminService adminService;
    private Invoice invoice;
    private final ClientInvoiceService clientInvoiceService;

    @Autowired
    BucketController(InvoiceRepo invoiceRepo, AmazonClient amazonClient, AdminService adminService, ClientInvoiceService clientInvoiceService) {
        this.amazonClient = amazonClient;
        this.adminService = adminService;
        this.clientInvoiceService = clientInvoiceService;
        this.invoiceRepo = invoiceRepo;
    }


    @PostMapping("/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = amazonClient.uploadFile(file);

        try {
            var detectedText = amazonClient.detectTextOnImg(fileName);
            invoice = adminService.prepareReadData(detectedText);
        }catch (Exception e){
            log.error("error during reading invoice...");
           invoice = new Invoice();
        }
        invoice.setInvoiceURL("https://enigeering.s3.us-east-2.amazonaws.com/" + fileName);
        invoice.setInvoiceUUID(UUID.randomUUID().toString());
        log.info("setting invoice url {}", invoice.getInvoiceURL());
        log.info("File with name {} has been uploaded", file.getName());
    }

    @PostMapping("saveInvInClient")
    public void saveClientInv(@RequestBody Invoice inv) {
        invoiceRepo.findByFvNumber(inv.getFvNumber()).ifPresent(t -> {
            log.error("Following invoice already exists in database");
            throw new IllegalArgumentException(t.toString());
        });
        inv.setInvoiceDate(LocalDate.now().toString());
        clientInvoiceService.checkClient(inv);
        clientInvoiceService.saveInvoice(inv);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

    @GetMapping("inv")
    public Invoice inv() {
        return invoice;
    }

}