package com.example.demo.admin.service;

import com.example.demo.elasticRepo.ClientRepoElastic;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.Set;
import static java.util.stream.Collectors.toSet;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientRepository clientRepository;
    private final ClientRepoElastic clientRepoElastic;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ClientRepository clientRepository,ClientRepoElastic clientRepoElastic) {
        this.projectRepository = projectRepository;
        this.clientRepository = clientRepository;
        this.clientRepoElastic = clientRepoElastic;
    }

    private Set<Client> addProjectAndAssignPeople(Project project) {

        return clientRepository.findAll()
                .stream()
                .filter(date -> date.getIsBusy() != null ? date.getIsBusy() : false)
                .filter(skills -> {
                    boolean skillsPresent = false;
                    for (String s : project.getTechnologies()) {
                        skillsPresent = skills.getSkills().contains(s);
                        if (!skillsPresent) {
                            break;
                        }
                    }
                    return skillsPresent;
                }).limit(project.getPeopleNeeded())
                .peek(c -> {
                   var client =  clientRepository.findById(c.getClientId()).get();
                    client.setStartProject(project.getStartDate());
                    client.setIsBusy(true);
                    client.addProject(project);
                    clientRepository.save(client);
                })
                  .collect(toSet());
    }
    public Project addProject(Project project){
        project.setEmployeesOnProject(this.addProjectAndAssignPeople(project));
        return projectRepository.save(project);
    }
}