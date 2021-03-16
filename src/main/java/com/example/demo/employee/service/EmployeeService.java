package com.example.demo.employee.service;

import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ExternalClientRepo;
import com.example.demo.security.model.User;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.service.UserCreateService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final UserCreateService userCreateService;
    private final ExternalClientRepo externalClientRepo;

    @Autowired
    public EmployeeService(ClientRepository clientRepository, UserRepository userRepository, UserCreateService userCreateService, ExternalClientRepo externalClientRepo) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.userCreateService = userCreateService;
        this.externalClientRepo=externalClientRepo;
    }

    public User createUser(User user) {
        var clientIsPresentInRepo = clientRepository.findByUsername(user.getUsername());
        var usernameIsAlreadyTaken = !userRepository.findUserByUsername(user.getUsername()).isPresent();
        if (clientIsPresentInRepo.isPresent() && usernameIsAlreadyTaken) {
            return userCreateService.addWithDefaultRole(user, clientIsPresentInRepo.get().getUserType());
        } else throw new IllegalArgumentException("error during creating account username: " + user.getUsername());
    }

    public List<ExternalClient> allExternals(){
        return externalClientRepo.findAll();
    }

    public ExternalClient getExternalClient(String id){
        return externalClientRepo.findById(id).orElse(null);
    }

    public Set<Invoice> getExternalClientInvoices(String id){
        return getExternalClient(id).getExternalClientInvoices();
    }
}
