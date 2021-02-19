package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.mail.MailService;
import com.example.demo.model.Client;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@Slf4j
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


    @GetMapping("/test")
    public String str() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @PostMapping("createUser")
    public Client createClient(@RequestBody Client client) {

        client.setUsername(adminService.makeUserName(client.getName(), client.getSurname()));
        mailService.sendSimpleMail(client.getEmail(), "Utworzono konto", "zaloguj sie na stronę: http://localhost:4200 przy użyciu username: " + client.getUsername());
        log.info("user with username, {} has been created",client.getUsername());
        log.info("mail has been sent do mail {}", client.getEmail());

        Optional<Client> isAlreadyPresent = clientRepository.findAll().stream().filter(n->n.getNIP().equals(client.getNIP())).findAny();
        if(isAlreadyPresent.isPresent()) {
         log.error("such client already exist in database");
         throw new IllegalArgumentException();
        }
        return clientRepository.save(client);

    }


}