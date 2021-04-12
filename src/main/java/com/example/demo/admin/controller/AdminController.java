package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.admin.service.InvoiceReport;
import com.example.demo.admin.service.ProjectService;
import com.example.demo.mail.MailService;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
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
    private final InvoiceReport invoiceReport;

    @Autowired
    public AdminController(MailService mailService, AdminService adminService, ClientRepository clientRepository, ProjectService projectService,
                           InvoiceReport invoiceReport) {
        this.mailService = mailService;
        this.adminService = adminService;
        this.clientRepository = clientRepository;
        this.projectService = projectService;
        this.invoiceReport = invoiceReport;
    }


    @GetMapping("/test")
    public String str() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("createUser")
    public Client createClient(@RequestBody Client client) {

        client.setUsername(adminService.makeUserName(client.getName(), client.getSurname()));
        mailService.sendSimpleMail(client.getEmail(), "Utworzono konto", "zaloguj sie na stronę: http://localhost:4200 przy użyciu username: " + client.getUsername());
        log.info("user with username, {} has been created", client.getUsername());
        log.info("mail has been sent do mail {}", client.getEmail());

        Optional<Client> isAlreadyPresent = clientRepository.findAll().stream().filter(n -> n.getNIP().equals(client.getNIP())).findAny();
        if (isAlreadyPresent.isPresent()) {
            log.error("such client already exist in database");
            throw new IllegalArgumentException();
        }
        return clientRepository.save(client);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("/addProj")
    public Project addProjectRes(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("listAllEmployees/{page}/{size}")
    public List<Client> listAllEmployees(@PathVariable int page, @PathVariable int size) {
        return clientRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "name", "surname")).getContent();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("allEmpLength")
    public Integer empLen() {
        return clientRepository.findAll().size();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("employeeProjects/{id}")
    public Set<Object> employeeProjects(@PathVariable String id) {
        return projectService.findProjectsByEmployee(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("listProjects/{page}/{size}")
    public List<Project> projects(@PathVariable int page, @PathVariable int size) {
        return projectService.projectsList(page, size);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("listProjectsLength")
    public Long listProjectsLenght() {
        return projectService.projectsLength();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("project/{id}")
    public Project getProject(@PathVariable String id) throws Exception {
        return projectService.getProjectById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("endOfProject/{projectName}")
    public Optional<Project> setEndOfProject(@PathVariable String projectName) {
        return projectService.endProject(projectName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("getEmployeesToProject/{projectName}")
    public List<Client> clientsToProject(@PathVariable String projectName) {
        return projectService.findClientsToProject(projectName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @PostMapping("addEmpToSpecProj/{clientId}")
    public Client addClientToProject(@PathVariable String clientId, @RequestBody Project project) {
        return projectService.addEmployeeToSpecProject(clientId, project);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping("deleteEmployeeFromProject/{projectName}/{clientId}")
    public void deleteEmployeeFromProject(@PathVariable String clientId, @PathVariable String projectName) {
        projectService.removeEmployeeFromProject(projectName, clientId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("getReport/{from}/{to}")

    public ResponseEntity<?> getReport(@PathVariable String from, @PathVariable String to) throws IOException {
        invoiceReport.generateReport(from, to);
            File file = new File(invoiceReport.getPATH());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment;filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_PDF).contentLength(file.length())
                    .body(resource);
        }
}