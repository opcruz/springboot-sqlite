package com.demo.sqlite.models;

import com.demo.sqlite.dtos.EmployeeSignupRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String password_hash;

    public static Employee fromSignupDTO(EmployeeSignupRequestDTO signupDTO, String passwordHash) {
        return Employee.builder()
                .name(signupDTO.getName())
                .surnames(signupDTO.getSurnames())
                .email(signupDTO.getEmail())
                .password_hash(passwordHash)
                .build();
    }

}
