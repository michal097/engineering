package com.example.demo.mongoRepo;

import com.example.demo.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    Page<Project> findAll(Pageable pageable);
    Optional<Project> findProjectByProjectName(String name);
}
