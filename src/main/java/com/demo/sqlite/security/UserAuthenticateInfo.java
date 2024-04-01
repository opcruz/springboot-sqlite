package com.demo.sqlite.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.Authentication;

import java.util.List;

@Data
@Builder
public class UserAuthenticateInfo {
    private int userId;
    private String subject;
    private List<String> roles;

    public static UserAuthenticateInfo fromAuth(Authentication auth) {
        return (UserAuthenticateInfo) auth.getDetails();
    }

}
