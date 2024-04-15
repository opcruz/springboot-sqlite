package com.demo.sqlite.service.impl;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.request.ClientSignupRequestDTO;
import com.demo.sqlite.model.dto.request.EmployeeSignupRequestDTO;
import com.demo.sqlite.model.dto.request.LoginRequestDTO;
import com.demo.sqlite.model.dto.response.LoginResponseDTO;
import com.demo.sqlite.model.entity.Client;
import com.demo.sqlite.model.entity.Employee;
import com.demo.sqlite.repository.ClientRepository;
import com.demo.sqlite.repository.EmployeeRepository;
import com.demo.sqlite.security.JWTCoder;
import com.demo.sqlite.service.UserService;
import com.demo.sqlite.utils.Roles;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

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

    public UserServiceImpl(@Autowired EmployeeRepository employeeRepository,
                           @Autowired ClientRepository clientRepository) {
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
    }

    public Optional<LoginResponseDTO> login(LoginRequestDTO requestDTO, String role) throws ValidationError {
        if (!Roles.isValid(role)) {
            throw new ValidationError(String.format("Invalid role: %s", role));
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

    public Client signupClient(ClientSignupRequestDTO requestDTO) throws ValidationError {
        if (clientRepository.existEmail(requestDTO.getEmail()).isPresent()) {
            throw new ValidationError(String.format("Email already exists: %s", requestDTO.getEmail()));
        }
        md.reset();
        md.update(requestDTO.getPassword().getBytes());
        String passwordHash = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        Client newClient = Client.fromSignupDTO(requestDTO, passwordHash);
        return clientRepository.save(newClient);
    }

    public Employee signupEmployee(EmployeeSignupRequestDTO requestDTO) throws ValidationError {
        if (employeeRepository.existEmail(requestDTO.getEmail()).isPresent()) {
            throw new ValidationError(String.format("Email already exists: %s", requestDTO.getEmail()));
        }
        md.reset();
        md.update(requestDTO.getPassword().getBytes());
        String passwordHash = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        Employee newEmployee = Employee.fromSignupDTO(requestDTO, passwordHash);
        return employeeRepository.save(newEmployee);
    }


}
