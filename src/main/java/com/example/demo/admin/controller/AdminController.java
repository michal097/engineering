package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.ProjectService;
import com.example.demo.mail.MailService;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@Slf4j
public class AdminController {

    private final ClientRepository clientRepository;
    private final AdminService adminService;
    private final MailService mailService;
    private final ProjectService projectService;

    @Autowired
    public AdminController(MailService mailService, AdminService adminService, ClientRepository clientRepository, ProjectService projectService) {
        this.mailService = mailService;
        this.adminService = adminService;
        this.clientRepository = clientRepository;
        this.projectService=projectService;
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

    @PostMapping("/addProj")
    public Project addProjectRes(@RequestBody Project project){
        return projectService.addProject(project);
    }

    @GetMapping("listAllEmployees/{page}/{size}")
    public List<Client> listAllEmployees(@PathVariable int page, @PathVariable int size){
        return clientRepository.findAll(PageRequest.of(page,size, Sort.Direction.ASC, "name","surname")).getContent();
    }
}