package com.demo.sqlite.models;

import com.demo.sqlite.dtos.ClientSignupRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surnames;
    private String direction;
    private String state;
    private String postal_code;
    private String phone;
    private String email;
    private String password_hash;

    public static Client fromSignupDTO(ClientSignupRequestDTO clientSignupDTO, String passwordHash) {
        return Client.builder()
                .name(clientSignupDTO.getName())
                .surnames(clientSignupDTO.getSurnames())
                .direction(clientSignupDTO.getDirection())
                .state(clientSignupDTO.getState())
                .postal_code(clientSignupDTO.getPostal_code())
                .phone(clientSignupDTO.getPhone())
                .email(clientSignupDTO.getEmail())
                .password_hash(passwordHash)
                .build();
    }

}
