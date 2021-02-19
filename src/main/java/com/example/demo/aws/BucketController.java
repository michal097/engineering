package com.example.demo.aws;


import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.ClientInvoiceService;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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


    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        amazonClient.uploadFile(file);
        invoice = adminService.prepareReadedData(amazonClient.detectTextOnImg(amazonClient.generateFileName(file)));
        log.info("File with name {} has been uploaded", file.getName());
    }

    @GetMapping("saveInvInClient")
    public void saveClientInv() {
        invoiceRepo.findByFvNumber(invoice.getFvNumber()).ifPresent(t -> {
            log.error("Following invoice already exists in database");
            throw new IllegalArgumentException(t.toString());
        });
        clientInvoiceService.saveInvoice(invoice);
        clientInvoiceService.checkClient(invoice);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

}