package com.example.demo.security.repository;

import com.example.demo.security.model.Role;
import com.example.demo.security.model.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
    UserRole findByRole(Role role);
}
