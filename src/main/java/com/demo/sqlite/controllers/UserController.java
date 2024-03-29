package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.LoginRequestDTO;
import com.demo.sqlite.dtos.LoginResponseDTO;
import com.demo.sqlite.repositories.ClientRepository;
import com.demo.sqlite.repositories.EmployeeRepository;
import com.demo.sqlite.utils.JWTCoder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.util.Collections;

@RestController
@RequestMapping(value = "/users")
public class UserController {

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

    public UserController(@Autowired EmployeeRepository employeeRepository,
                          @Autowired ClientRepository clientRepository) {
        this.employeeRepository = employeeRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/employee/login")
    @Operation(summary = "Login employees")
    public @ResponseBody ResponseEntity<LoginResponseDTO> employeeLogin(@RequestBody LoginRequestDTO body) {
        md.reset();
        md.update(body.getPassword().getBytes());
        String passwordHash = DatatypeConverter
                .printHexBinary(md.digest()).toUpperCase();
        return employeeRepository.loginEmployee(body.getEmail(), passwordHash).map(employee -> {
            String token = getJWTToken(body.getEmail(), "employee", employee.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setEmail(body.getEmail());
            loginResponseDTO.setToken(token);

            return ResponseEntity.ok().body(loginResponseDTO);
        }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/client/login")
    @Operation(summary = "Login client")
    public @ResponseBody ResponseEntity<LoginResponseDTO> clientLogin(@RequestBody LoginRequestDTO body) {
        md.reset();
        md.update(body.getPassword().getBytes());
        String passwordHash = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        return clientRepository.loginClient(body.getEmail(), passwordHash).map(client -> {
            String token = getJWTToken(body.getEmail(), "client", client.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setEmail(body.getEmail());
            loginResponseDTO.setToken(token);

            return ResponseEntity.ok().body(loginResponseDTO);
        }).orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private String getJWTToken(String email, String role, int id) {
        return JWTCoder.generateJWT(email, Collections.singletonList(role), id);
    }

}
