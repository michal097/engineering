package com.example.demo.employee.controller;

import com.example.demo.employee.service.HolidayProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class HolidayProposalController {

    private final HolidayProposalService holidayProposalService;

    @Autowired
    public HolidayProposalController(HolidayProposalService holidayProposalService){
        this.holidayProposalService=holidayProposalService;
    }

    @PostMapping(value = "/uploadHolidayProposal")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file) {

        holidayProposalService.recognizeHolidayProposal(file);

    }
}
