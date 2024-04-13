package com.demo.sqlite.models;

import com.demo.sqlite.dtos.EmployeeSignupRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surnames;
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;

    public static Employee fromSignupDTO(EmployeeSignupRequestDTO signupDTO, String passwordHash) {
        return Employee.builder()
                .name(signupDTO.getName())
                .surnames(signupDTO.getSurnames())
                .email(signupDTO.getEmail())
                .passwordHash(passwordHash)
                .build();
    }

}
