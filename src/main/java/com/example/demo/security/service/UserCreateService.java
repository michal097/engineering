package com.example.demo.security.service;


import com.example.demo.security.model.User;
import com.example.demo.security.model.UserRole;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.security.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreateService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserCreateService(PasswordEncoder passwordEncoder,
                             UserRepository userRepository,
                             UserRoleRepository userRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public User addWithDefaultRole(User user, String roleName) {
        UserRole defaultRole = userRoleRepository.findByRole(roleName);
        user.getRoles().add(defaultRole);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordHash);

        return userRepository.save(user);

    }
}
