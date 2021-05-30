package com.example.demo.security.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "roles")
@Data
public class UserRole {


    @Id
    private String roleId;
    private Role role;
    private String description;

    public UserRole(Role role, String description){
        this.role=role;
        this.description=description;
    }

}