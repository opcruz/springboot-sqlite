package com.demo.sqlite.model.entity;

import com.demo.sqlite.model.dto.request.ClientSignupRequestDTO;
import jakarta.persistence.*;
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
   @Column(name = "postal_code")
   private String postalCode;
   private String phone;
   private String email;
   @Column(name = "password_hash")
   private String passwordHash;

   public static Client fromSignupDTO(ClientSignupRequestDTO clientSignupDTO, String passwordHash) {
      return Client.builder().name(clientSignupDTO.getName())
            .surnames(clientSignupDTO.getSurnames()).direction(clientSignupDTO.getDirection())
            .state(clientSignupDTO.getState()).postalCode(clientSignupDTO.getPostal_code())
            .phone(clientSignupDTO.getPhone()).email(clientSignupDTO.getEmail())
            .passwordHash(passwordHash).build();
   }

}
