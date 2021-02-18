package com.example.demo.employee.controller;

import com.example.demo.employee.service.EmployeeService;
import com.example.demo.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("createAndSaveUser")
    public User createAndSaveUser(@RequestBody User user) {
        return employeeService.createUser(user);

    }
}
