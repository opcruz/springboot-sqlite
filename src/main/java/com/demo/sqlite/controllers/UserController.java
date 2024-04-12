package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.ClientSignupRequestDTO;
import com.demo.sqlite.dtos.EmployeeSignupRequestDTO;
import com.demo.sqlite.dtos.LoginRequestDTO;
import com.demo.sqlite.dtos.LoginResponseDTO;
import com.demo.sqlite.exceptions.ValidationError;
import com.demo.sqlite.models.Client;
import com.demo.sqlite.models.Employee;
import com.demo.sqlite.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login employees")
    public @ResponseBody ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO requestDTO,
            @Parameter(
                    name = "role",
                    description = "Role of user",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", allowableValues = {"client", "employee"}),
                    example = "client")
            @RequestParam(defaultValue = "client") String role
    ) {
        try {
            return userService.login(requestDTO, role)
                    .map(loginResponseDTO -> ResponseEntity.ok().body(loginResponseDTO))
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        } catch (ValidationError e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @PostMapping("/client/signup")
    @Operation(summary = "Signup new client")
    public @ResponseBody ResponseEntity<Map<String, String>> signupClient(@Valid @RequestBody ClientSignupRequestDTO requestDTO) {
        try {
            Client newClient = userService.signupClient(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", String.valueOf(newClient.getId()),
                            "name", newClient.getName(),
                            "email", newClient.getEmail()
                    ));
        } catch (ValidationError e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/employee/signup")
    @Operation(summary = "Signup new employee")
    public @ResponseBody ResponseEntity<Map<String, String>> signupEmployee(@Valid @RequestBody EmployeeSignupRequestDTO requestDTO) {
        try {
            Employee newEmployee = userService.signupEmployee(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", String.valueOf(newEmployee.getId()),
                            "name", newEmployee.getName(),
                            "email", newEmployee.getEmail()
                    ));
        } catch (ValidationError e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("message", e.getMessage()));
        }
    }

}
