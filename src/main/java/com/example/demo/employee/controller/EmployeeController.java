package com.example.demo.employee.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.employee.service.EmployeeService;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.mongoRepo.ProjectRepository;
import com.example.demo.security.model.User;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RestController
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ClientRepository clientRepository;
    private final ClientRepoElastic clientRepoElastic;
    private final InvoiceRepo invoiceRepo;
    private final ProjectRepository projectRepository;
    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              ClientRepository clientRepository,
                              ClientRepoElastic clientRepoElastic,
                              InvoiceRepo invoiceRepo,
                              ProjectRepository projectRepository) {
        this.employeeService = employeeService;
        this.clientRepository = clientRepository;
        this.clientRepoElastic = clientRepoElastic;
        this.invoiceRepo=invoiceRepo;
        this.projectRepository=projectRepository;
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
    public Object deleteEmployee(@PathVariable String id) {
        var client = clientRepository.findById(id);

        return client.map(value -> projectRepository.findAll().stream().filter(p -> p.getEmployeesOnProject().contains(value))
                .map(cOnPro -> {
                    cOnPro.getEmployeesOnProject().remove(value);
                    clientRepository.deleteById(id);
                    clientRepoElastic.deleteById(id);
                    return projectRepository.save(cOnPro);
                })).orElse(null);

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

    @GetMapping("clientInvoices/{id}")
    public Set<Invoice> clientInvoices(@PathVariable String id){
        return clientRepository.findById(id)
                .stream()
                .flatMap(client -> client.getClientInvoices() !=null ? client.getClientInvoices().stream() : Stream.of()).collect(toSet());
    }
    @GetMapping("findInvoiceByNumber/{invoiceNumber}")
    public String findInvoiceUrl(@PathVariable String invoiceNumber){

        final String[] invURL = {""};
        invoiceRepo.findByFvNumber(invoiceNumber.replaceAll("_","/")).ifPresent(c-> invURL[0] = (c.getInvoiceURL()));
        return invURL[0];
    }

    @GetMapping("allExternals")
    public List<ExternalClient> allExternals(){
        return employeeService.allExternals();
    }

    @GetMapping("getExternal/{id}")
    public ExternalClient getExternal(@PathVariable String id){
        return employeeService.getExternalClient(id);
    }

    @GetMapping("getExternalClientInvoices/{id}")
    public Set<Invoice> externalInvoices(@PathVariable String id){
        return employeeService.getExternalClientInvoices(id);
    }



}
