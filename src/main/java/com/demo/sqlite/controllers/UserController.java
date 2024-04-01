package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.LoginRequestDTO;
import com.demo.sqlite.dtos.LoginResponseDTO;
import com.demo.sqlite.exceptions.ValidationError;
import com.demo.sqlite.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestBody LoginRequestDTO requestDTO,
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

}
