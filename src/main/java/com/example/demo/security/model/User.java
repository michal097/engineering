package com.example.demo.security.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String userId;
    private String username;
    private String password;

    @DBRef
    private Set<UserRole> roles = new HashSet<>();

}