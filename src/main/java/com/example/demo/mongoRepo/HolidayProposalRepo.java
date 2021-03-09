package com.example.demo.mongoRepo;

import com.example.demo.model.HolidayProposal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HolidayProposalRepo extends MongoRepository<HolidayProposal, String> {
}
