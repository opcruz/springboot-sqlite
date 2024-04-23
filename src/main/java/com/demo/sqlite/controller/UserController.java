package com.demo.sqlite.controller;

import com.demo.sqlite.model.dto.request.ClientSignupRequestDTO;
import com.demo.sqlite.model.dto.request.EmployeeSignupRequestDTO;
import com.demo.sqlite.model.dto.request.LoginRequestDTO;
import com.demo.sqlite.model.dto.response.LoginResponseDTO;
import com.demo.sqlite.model.entity.Client;
import com.demo.sqlite.model.entity.Employee;
import com.demo.sqlite.service.UserService;
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
    @Operation(summary = "Login user")
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
        return userService.login(requestDTO, role)
                .map(loginResponseDTO -> ResponseEntity.ok().body(loginResponseDTO))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }

    @PostMapping("/clients/signup")
    @Operation(summary = "Signup new client")
    public @ResponseBody ResponseEntity<Map<String, String>> signupClient(@Valid @RequestBody ClientSignupRequestDTO requestDTO) {
        Client newClient = userService.signupClient(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", String.valueOf(newClient.getId()),
                        "name", newClient.getName(),
                        "email", newClient.getEmail()
                ));
    }

    @PostMapping("/employees/signup")
    @Operation(summary = "Signup new employee")
    public @ResponseBody ResponseEntity<Map<String, String>> signupEmployee(@Valid @RequestBody EmployeeSignupRequestDTO requestDTO) {
        Employee newEmployee = userService.signupEmployee(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", String.valueOf(newEmployee.getId()),
                        "name", newEmployee.getName(),
                        "email", newEmployee.getEmail()
                ));
    }

}
