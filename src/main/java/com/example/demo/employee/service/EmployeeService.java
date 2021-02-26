package com.example.demo.employee.service;

import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.security.model.Role;
import com.example.demo.security.model.User;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.service.UserCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final UserCreateService userCreateService;

    @Autowired
    public EmployeeService(ClientRepository clientRepository, UserRepository userRepository, UserCreateService userCreateService) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.userCreateService = userCreateService;
    }

    public User createUser(User user) {

        if (clientRepository.findByUsername(user.getUsername()).isPresent()
            &&
            !userRepository.findUserByUsername(user.getUsername()).isPresent()) {

            return userCreateService.addWithDefaultRole(user, Role.ROLE_USER.name());
        }
         else throw new IllegalArgumentException("error during creating account username: " + user.getUsername() );
    }



}
