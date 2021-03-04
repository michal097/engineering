package com.example.demo.employee.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.employee.service.EmployeeService;
import com.example.demo.model.Client;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ClientRepository clientRepository;
    private final ClientRepoElastic clientRepoElastic;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ClientRepository clientRepository, ClientRepoElastic clientRepoElastic) {
        this.employeeService = employeeService;
        this.clientRepository = clientRepository;
        this.clientRepoElastic = clientRepoElastic;
    }

    @PostMapping("createAndSaveUser")
    public User createAndSaveUser(@RequestBody User user) {
        return employeeService.createUser(user);

    }

    @GetMapping("getEmployee/{id}")
    public Client getClient(@PathVariable String id) throws Exception {
        return clientRepository.findById(id).orElseThrow(Exception::new);
    }

    @DeleteMapping("deleteEmployee/{id}")
    public void deleteEmployee(@PathVariable String id) {
        clientRepository.deleteById(id);
        clientRepoElastic.deleteById(id);
    }

    @PutMapping("updateEmployee/{id}")
    public Client updateClient(@RequestBody Client client, @PathVariable String id) {
        return clientRepository.findById(id)
                .map(c -> {
                    c.setName(client.getName());
                    c.setSurname(client.getSurname());
                    c.setAdress(client.getAdress());
                    c.setEmail(client.getEmail());
                    c.setNIP(client.getNIP());
                    c.setSkills(client.getSkills());
                    clientRepoElastic.findById(id).map(e ->
                            clientRepoElastic.save(c)
                    );
                    return clientRepository.save(c);
                }).orElseGet(
                        () -> {
                            client.setClientId(id);
                            return clientRepository.save(client);
                        }
                );
    }
}
