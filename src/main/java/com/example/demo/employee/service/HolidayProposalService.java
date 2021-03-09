package com.example.demo.employee.service;

import com.example.demo.aws.AmazonClient;
import com.example.demo.mongoRepo.ClientRepository;
import com.example.demo.mongoRepo.HolidayProposalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class HolidayProposalService {

    private final HolidayProposalRepo holidayProposalRepo;
    private final ClientRepository clientRepository;
    private final AmazonClient amazonClient;
    @Autowired
    public HolidayProposalService(HolidayProposalRepo holidayProposalRepo, ClientRepository clientRepository, AmazonClient amazonClient){
        this.holidayProposalRepo=holidayProposalRepo;
        this.clientRepository=clientRepository;
        this.amazonClient=amazonClient;
    }

    public void recognizeHolidayProposal(MultipartFile file){
        amazonClient.detectTextOnImg(amazonClient.generateFileName(file)).forEach(System.out::println);
    }


}
