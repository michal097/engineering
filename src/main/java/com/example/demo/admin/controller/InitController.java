package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.elasticRepo.ExtClientRepoElastic;
import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ExternalClientRepo;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.mongoRepo.ProjectRepository;
import com.example.demo.security.model.Role;
import com.example.demo.security.model.User;
import com.example.demo.security.model.UserRole;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import com.example.demo.security.service.UserCreateService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
public class InitController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCreateService userCreateService;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    InvoiceRepoElastic invoiceRepo;
    @Autowired
    InvoiceRepo invoiceRepo1;
    @Autowired
    ExtClientRepoElastic extClientRepoElastic;
    @Autowired
    ExternalClientRepo externalClientRepo;
    @Autowired
    ClientRepository clientRepository;

    @GetMapping("setRoles")
    public String setRolesInRepoLALALA() {
        var myRoles = List.of(new UserRole(Role.ROLE_ADMIN, "ROLA dla admina"),
                new UserRole(Role.ROLE_USER, "Rola dla usera"));
        userRoleRepository.saveAll(myRoles);
        return "Role zostały zapisane";
    }

    @GetMapping("delUsers")
    public String s() {
        userRepository.deleteAll();
        return "asd";
    }

    @GetMapping("roo")
    public List<UserRole> getRoo() {
        return userRoleRepository.findAll();
    }

    @GetMapping("delRoo")
    public String yo() {
        userRoleRepository.deleteAll();
        return "";
    }

    @GetMapping("create")
    public String createUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        userCreateService.addWithDefaultRole(user, Role.ROLE_ADMIN.name());
        userRepository.save(user);

        return "hello";
    }

    @GetMapping("getRepo")
    public List<String> inv() {
        return invoiceRepo.findAll().stream().map(Invoice::getNIP).distinct().collect(toList());
    }
    @GetMapping("delInv")
    public String as(){
        invoiceRepo1.deleteAll();
        return "asd";
    }
    @GetMapping("ext")
    public List<ExternalClient> e(){
        return extClientRepoElastic.findAll(PageRequest.of(0,100)).getContent();

    }

    @GetMapping("cleanElastic")
    public void cleanMe(){

    }
    @GetMapping("invs")
    public List<Invoice> iins(){
        invoiceRepo.deleteAll();
        return invoiceRepo.findAll();
    }

    @GetMapping("client")
    public Client c(){
        return clientRepository.findAll().get(0);
    }

    @GetMapping("clDate")
    public void ddsadasd()
    {
        clientRepository.findAll().forEach(s->{
                s.setEndProject(LocalDate.of(2020,1,1));
        });
    }

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ClientRepoElastic clientRepoElastic;
    @GetMapping("clBusy")
    public void makeBusy(){

        extClientRepoElastic.deleteAll();
        clientRepoElastic.deleteAll();
        invoiceRepo.deleteAll();

    }

    @GetMapping("clEla")
    public List<Client> ccccc(){
        return clientRepoElastic.findAll();
    }

    @GetMapping("userDetails")
    public String getUserDetails(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
