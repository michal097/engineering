package com.example.demo.mongoRepo;

import com.example.demo.model.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IssueRepo extends MongoRepository<Issue, String> {

    List<Issue> findAll();
}
