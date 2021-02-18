package com.example.demo.aws;


import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.ClientInvoiceService;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin
@RestController
public class BucketController {

    private final AmazonClient amazonClient;
    private final AdminService adminService;
    private Invoice invoice;
    private final ClientInvoiceService clientInvoiceService;
    private final InvoiceRepo invoiceRepo;

    @Autowired
    BucketController(AmazonClient amazonClient, AdminService adminService, ClientInvoiceService clientInvoiceService, InvoiceRepo invoiceRepo) {
        this.amazonClient = amazonClient;
        this.adminService = adminService;
        this.clientInvoiceService = clientInvoiceService;
        this.invoiceRepo=invoiceRepo;
    }


    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file) {
        amazonClient.uploadFile(file);
        invoice = adminService.prepareReadedData(amazonClient.detectTextOnImg(amazonClient.generateFileName(file)));

    }

    @GetMapping("saveInvInClient")
    public void saveClientInv(){
        clientInvoiceService.saveInvoice(invoice);
        clientInvoiceService.checkClient(invoice);
    }

    @GetMapping("inv")
    public Invoice invoice() {
        return this.invoice;
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

    @GetMapping("myData/{img}")
    public List<String> getData(@PathVariable String img) {
        System.out.println("Imma here");

        return amazonClient.detectTextOnImg(img);
    }
}