package com.demo.sqlite.models;

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

}
