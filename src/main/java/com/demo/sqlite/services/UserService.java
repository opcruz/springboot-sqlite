package com.demo.sqlite.services;

import com.demo.sqlite.dtos.LoginRequestDTO;
import com.demo.sqlite.dtos.LoginResponseDTO;
import com.demo.sqlite.repositories.ClientRepository;
import com.demo.sqlite.repositories.EmployeeRepository;
import com.demo.sqlite.security.JWTCoder;
import com.demo.sqlite.utils.Roles;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.demo.sqlite.exceptions.ValidationError;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private final EmployeeRepository employeeRepository;
    private final ClientRepository clientRepository;

    public UserService(@Autowired EmployeeRepository employeeRepository,
                       @Autowired ClientRepository clientRepository) {
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
    }

    public Optional<LoginResponseDTO> login(LoginRequestDTO requestDTO, String role) throws ValidationError {
        if (!Roles.isValid(role)) {
           throw new ValidationError();
        }
        md.reset();
        md.update(requestDTO.getPassword().getBytes());
        String passwordHash = DatatypeConverter
                .printHexBinary(md.digest()).toUpperCase();

        Optional<LoginResponseDTO> loginResponseDTOOptional;
        if (Roles.CLIENT.getRole().equalsIgnoreCase(role)) {
            loginResponseDTOOptional =
                    clientRepository.findClientByEmailAndPassword(requestDTO.getEmail(), passwordHash)
                            .map(client -> {
                                String token = getJWTToken(requestDTO.getEmail(), Roles.CLIENT.getRoleWithPrefix(), client.getId());
                                return LoginResponseDTO.builder()
                                        .email(requestDTO.getEmail())
                                        .token(token)
                                        .build();
                            });
        } else if (Roles.EMPLOYEE.getRole().equalsIgnoreCase(role)) {
            loginResponseDTOOptional =
                    employeeRepository.findEmployeeByEmailAndPassword(requestDTO.getEmail(), passwordHash)
                            .map(client -> {
                                String token = getJWTToken(requestDTO.getEmail(), Roles.EMPLOYEE.getRoleWithPrefix(), client.getId());
                                return LoginResponseDTO.builder()
                                        .email(requestDTO.getEmail())
                                        .token(token)
                                        .build();
                            });
        } else {
            loginResponseDTOOptional = Optional.empty();
        }
        return loginResponseDTOOptional;

    }

    private String getJWTToken(String email, String role, int id) {
        return JWTCoder.generateJWT(email, Collections.singletonList(role), id);
    }


}
