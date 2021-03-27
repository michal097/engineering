package com.example.demo.employee.controller;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.employee.service.EmployeeService;
import com.example.demo.model.Client;
import com.example.demo.model.ExternalClient;
import com.example.demo.model.Invoice;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.InvoiceRepo;
import com.example.demo.mongoRepo.ProjectRepository;
import com.example.demo.security.model.User;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@RestController
@CrossOrigin
@Slf4j
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
        this.invoiceRepo = invoiceRepo;
        this.projectRepository = projectRepository;
    }


    @PostMapping("createAndSaveUser")
    public User createAndSaveUser(@RequestBody User user) {
        return employeeService.createUser(user);

    }

    @GetMapping("getEmployeeById/{id}")
    public Client getClient(@PathVariable String id) throws Exception {
        return clientRepository.findById(id).orElseThrow(Exception::new);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @DeleteMapping("deleteEmployee/{id}")
    public Object deleteEmployee(@PathVariable String id) {

        var client = clientRepository.findById(id);

        if (client.isPresent() && client.get().getProjects() == null) {
            clientRepository.deleteById(id);
            clientRepoElastic.deleteById(id);
        } else {
            return client.map(c -> projectRepository.findAll().stream().filter(p -> findClientsOnProject(c, p))
                    .map(cOnPro -> {
                        var cc = cOnPro.getEmployeesOnProject()
                                .stream()
                                .filter(e -> e.getClientId()
                                        .equals(c.getClientId()))
                                .findAny();
                        cc.ifPresent(value -> cOnPro.getEmployeesOnProject().remove(value));
                        clientRepository.deleteById(id);
                        clientRepoElastic.deleteById(id);
                        return projectRepository.save(cOnPro);
                    })).orElse(null);
        }
        return "";
    }

    public boolean findClientsOnProject(Client client, Project project) {
        Set<Client> cc = project.getEmployeesOnProject();
        return cc.stream().anyMatch(c -> c.getClientId().equals(client.getClientId()));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
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
                    try {
                        clientRepoElastic.findById(c.getClientId()).map(e -> {
                                    e.setName(c.getName());
                                    e.setSurname(c.getSurname());
                                    e.setAdress(c.getAdress());
                                    e.setEmail(c.getEmail());
                                    e.setNIP(c.getNIP());
                                    e.setSkills(c.getSkills());
                                    return clientRepoElastic.save(e);
                                }
                        );
                    } catch (Exception e) {
                        log.error("error during update user");
                    }
                    return clientRepository.save(c);
                }).orElseGet(
                        () -> {
                            client.setClientId(id);
                            return clientRepository.save(client);
                        }
                );
    }

    @GetMapping("clientInvoices/{id}")
    public Set<Invoice> clientInvoices(@PathVariable String id) {
        return clientRepository.findById(id)
                .stream()
                .flatMap(client -> client.getClientInvoices() != null ? client.getClientInvoices().stream() : Stream.of()).collect(toSet());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("findInvoiceByNumber/{invoiceNumber}")
    public String findInvoiceUrl(@PathVariable String invoiceNumber) {

        final String[] invURL = {""};
        invoiceRepo.findByFvNumber(invoiceNumber.replaceAll("_", "/")).ifPresent(c -> invURL[0] = (c.getInvoiceURL()));
        return invURL[0];
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("allExternals/{page}/{size}")
    public List<ExternalClient> allExternals(@PathVariable int page, @PathVariable int size) {
        return employeeService.allExternals(page, size);
    }

    @GetMapping("externalSize")
    public long getExternalListSize() {
        return employeeService.getAllExternals();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getExternal/{id}")
    public ExternalClient getExternal(@PathVariable String id) {
        return employeeService.getExternalClient(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getExternalClientInvoices/{id}")
    public Set<Invoice> externalInvoices(@PathVariable String id) {
        return employeeService.getExternalClientInvoices(id);
    }

    @GetMapping("countAllExternalClients")
    public long getAllExternalUsersCount() {
        return employeeService.countAllExternalClients();
    }

}
