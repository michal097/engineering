package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.mongoRepo.*;
import com.example.demo.security.model.Role;
import com.example.demo.security.model.User;
import com.example.demo.security.model.UserRole;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import com.example.demo.security.service.UserCreateService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin
public class InitController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCreateService userCreateService;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private InvoiceRepoElastic invoiceRepo;
    @Autowired
    private InvoiceRepo invoiceRepo1;
    @Autowired
    private ExtClientRepoElastic extClientRepoElastic;
    @Autowired
    private ExternalClientRepo externalClientRepo;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private IssueRepo issueRepo;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ClientRepoElastic clientRepoElastic;




    @GetMapping("ext")
    public List<ExternalClient> clientRepoElastics(){
        return extClientRepoElastic.findAll();
    }
    @GetMapping("cl")
    public List<Client> clientRepoElasticsC(){
        return clientRepoElastic.findAll();
    }

    @GetMapping("delEverything")
    public String kaboom() {

        //delete all roles
        userRoleRepository.deleteAll();
        //delete all users
        userRepository.deleteAll();
        //create roles
        var myRoles = List.of(new UserRole(Role.ROLE_ADMIN, "ROLA dla admina"),
                new UserRole(Role.ROLE_USER, "Rola dla usera"),
                new UserRole(Role.ROLE_MODERATOR, "Rola dla moderatora"));
        userRoleRepository.saveAll(myRoles);
        //create admin user
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        userCreateService.addWithDefaultRole(user, Role.ROLE_ADMIN);
        userRepository.save(user);

        //delete everything else
        clientRepoElastic.deleteAll();
        extClientRepoElastic.deleteAll();
        invoiceRepo.deleteAll();
        clientRepository.deleteAll();
        externalClientRepo.deleteAll();
        invoiceRepo1.deleteAll();
        issueRepo.deleteAll();
        projectRepository.deleteAll();
        return "kaboom kurwa";
    }
}
