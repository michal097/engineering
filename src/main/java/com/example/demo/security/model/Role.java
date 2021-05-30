package com.example.demo.security.model;

public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_MODERATOR("ROLE_MODERATOR");

    private final String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    @Override
    public String toString(){
        return this.roleName;
    }
}
