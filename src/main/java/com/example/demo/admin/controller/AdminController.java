package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.mail.MailService;
import com.example.demo.model.Client;
import com.example.demo.mongoRepo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AdminController {

    private final ClientRepository clientRepository;
    private final AdminService adminService;
    private final MailService mailService;

    @Autowired
    public AdminController(MailService mailService, AdminService adminService, ClientRepository clientRepository) {
        this.mailService = mailService;
        this.adminService = adminService;
        this.clientRepository = clientRepository;
    }


    @GetMapping("clien")
    public Client client() {
        return clientRepository.save(Client.builder().NIP("123123321").build());
    }

    @GetMapping("/test") //SHOWS ROLE
    public String str() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @PostMapping("createUser")
    public Client createClient(@RequestBody Client client) {

        client.setUsername(adminService.makeUserName(client.getName(), client.getSurname()));
        mailService.sendSimpleMail(client.getEmail(), "Utworzono konto", "zaloguj sie na stronę: http://localhost:4200 przy użyciu username: " + client.getUsername());
        return clientRepository.save(client);

    }


}