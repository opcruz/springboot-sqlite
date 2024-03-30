package com.demo.sqlite.utils;


public enum Roles {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE"),
    CLIENT("CLIENT");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + role;
    }

}
