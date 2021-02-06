package com.example.demo.controller;

import com.example.demo.security.model.Role;
import com.example.demo.security.model.User;
import com.example.demo.security.model.UserRole;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import com.example.demo.security.service.UserCreateService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin
public class AdminController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCreateService userCreateService;
    @Autowired
    UserRoleRepository userRoleRepository;
    @GetMapping("setRoles")
    public String setRolesInRepoLALALA(){
        var myRoles = List.of(new UserRole(Role.ROLE_ADMIN,"ROLA dla admina"),
                new UserRole(Role.ROLE_USER, "Rola dla usera"));
        userRoleRepository.saveAll(myRoles);
        return "Role zostały zapisane";
    }

    @GetMapping("roo")
    public List<UserRole> getRoo(){
        return userRoleRepository.findAll();
    }

    @GetMapping("create")
    public String createUser(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        userCreateService.addWithDefaultRole(user, "ROLE_ADMIN");
        userRepository.save(user);

        return "hello";
    }
    @GetMapping("poka")
    public List<User> users(){
        return userRepository.findAll();
    }
    @GetMapping("/test") //SHOWS ROLE
    public String str() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

}
