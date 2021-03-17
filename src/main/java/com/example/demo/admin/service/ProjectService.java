package com.example.demo.admin.service;

import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ClientRepository clientRepository) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
    }

    private Set<Client> addProjectAndAssignPeople(Project project) {

        return clientRepository.findAll()
                .stream()
                .filter(date -> date.getIsBusy() != null ? !date.getIsBusy() : date.setIsBusyInFilter())
                .filter(skills -> {
                    boolean skillsPresent = false;
                    for (String s : project.getTechnologies()) {
                        skillsPresent = skills.getSkills().contains(s);
                        if (!skillsPresent) {
                            break;
                        }
                    }
                    return skillsPresent;
                })
                .limit(project.getPeopleNeeded())
                .peek(c -> clientRepository.findById(c.getClientId())
                        .ifPresent(
                                cl -> {
                                    cl.addProject(project);
                                    cl.setStartProject(project.getStartDate());
                                    cl.setIsBusy(true);
                                    clientRepository.save(cl);
                                }
                        ))
                .collect(toSet());

    }

    public Project addProject(Project project) {
        boolean alreadyTaken = projectRepository.findProjectByProjectName(project.getProjectName()).isPresent();
        System.out.println(project);
        if (alreadyTaken) throw new IllegalArgumentException();
        project.setProjectName(project.getProjectName().trim());
        project.setEmployeesOnProject(this.addProjectAndAssignPeople(project));
        project.setEnded(false);
        return projectRepository.save(project);
    }

    public List<Project> projectsList(int page, int size) {
        return projectRepository.findAll(PageRequest.of(page, size)).getContent().stream().filter(p -> p.getEnded() != null && !p.getEnded()).collect(toList());
    }

    public long projectsLength() {
        return projectRepository.findAll().stream().filter(p -> p.getDeadLineDate() == null).count();
    }

    public Set<Object> findProjectsByEmployee(String clientId) {
        return clientRepository.findAll()
                .stream()
                .filter(emp -> emp.getProjects() != null && emp.getClientId().equals(clientId))
                .flatMap(c -> c.getProjects().stream())
                .collect(toSet());
    }

    public Project getProjectById(String projectName) throws Exception {
        var pName = projectName.replaceAll("-", " ").trim();
        return projectRepository.findAll().stream().filter(p -> p.getProjectName().equals(pName)).findFirst().orElseThrow(Exception::new);
    }

    private void changeBusyOnClient(Project p) {

        p.getEmployeesOnProject().forEach(c -> {
            c.setIsBusy(false);
            clientRepository.save(c);
        });
    }

    public List<Client> findClientsToProject(String projectName) {
        var project = projectRepository.findProjectByProjectName(projectName);
        var technologies = project.isPresent() ? project.get().getTechnologies() : new HashSet();
        var calculatePeopleOnProj = project.get().getPeopleNeeded() - project.get().getEmployeesOnProject().size();
        return clientRepository.findAll()
                .stream()
                .filter(c -> c.getIsBusy() != null ? !c.getIsBusy() : c.setIsBusyInFilter())
                .filter(c -> c.getSkills().containsAll(technologies))
                .limit(calculatePeopleOnProj)
                .collect(toList());
    }


    public Project endProject(String projectName) {
        projectRepository.findProjectByProjectName(projectName).map(project -> {
            this.changeBusyOnClient(project);
            project.setDeadLineDate(LocalDate.now());
            project.setEmployeesOnProject(new HashSet<>());
            project.setEnded(true);

            return projectRepository.save(project);
        });
        return null;
    }

    public Client addEmployeeToSpecProject(String clientId, Project project) {
        var client = clientRepository.findById(clientId);
        client.ifPresent(c -> {
            c.addProject(project);
            c.setIsBusy(true);
        });
        client.ifPresent(c -> this.updateProject(project, c));
        return client.map(clientRepository::save).orElse(null);
    }

    public void updateProject(Project project, Client client) {
        projectRepository.findProjectByProjectName(project.getProjectName()).map(p -> {
            p.getEmployeesOnProject().add(client);
            return projectRepository.save(p);
        });
    }
}