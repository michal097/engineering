package com.example.demo.mongoRepo;

import com.example.demo.model.IssueCounter;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IssueCounterRepo extends MongoRepository<IssueCounter, String> {
}
