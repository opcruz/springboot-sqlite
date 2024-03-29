package com.demo.sqlite.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "stock") // This tells Hibernate to make a table out of this class
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    private String description;
    @JsonIgnore
    private byte[] image;
    private String color;
    private String category;
    private Integer quantity;
    private Double price;
    private String status;
    private Integer created_by;
    private Integer updated_by;
    public Stock(Integer code,
                 String description,
                 String color,
                 String category,
                 Integer quantity,
                 Double price,
                 String status,
                 Integer created_by,
                 Integer updated_by) {
        this.code = code;
        this.description = description;
        this.color = color;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

}
