package com.example.demo.admin.controller;

import com.example.demo.elasticRepo.InvoiceRepoElastic;
import com.example.demo.model.Invoice;
import com.example.demo.security.model.User;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import com.example.demo.security.service.UserCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



//    @GetMapping("setRoles")
//    public String setRolesInRepoLALALA() {
//        var myRoles = List.of(new UserRole(Role.ROLE_ADMIN, "ROLA dla admina"),
//                new UserRole(Role.ROLE_USER, "Rola dla usera"));
//        userRoleRepository.saveAll(myRoles);
//        return "Role zostały zapisane";
//    }
//    @GetMapping("delUsers")
//    public String s(){
//        userRepository.deleteAll();
//        return "asd";
//    }
//
//    @GetMapping("roo")
//    public List<UserRole> getRoo() {
//        return userRoleRepository.findAll();
//    }
//
//    @GetMapping("delRoo")
//    public String yo(){
//        userRoleRepository.deleteAll();
//        return "";
//    }
    @GetMapping("create")
    public String createUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        userCreateService.addWithDefaultRole(user, "ROLE_ADMIN");
        userRepository.save(user);

        return "hello";
    }
    @Autowired
    InvoiceRepoElastic invoiceRepo;
    @GetMapping("getRepo")
    public List<String> inv(){
        return invoiceRepo.findAll().stream().map(Invoice::getNIP).distinct().collect(toList());
    }





}
