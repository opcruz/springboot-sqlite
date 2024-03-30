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

@Entity(name = "stock")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    private String description;
    @JsonIgnore
    private byte[] image;
    private int category_id;
    private int quantity;
    private double price;
    private String status;
    private int created_by;
    private int updated_by;
    public Stock(Integer code,
                 String description,
                 int category_id,
                 Integer quantity,
                 Double price,
                 String status,
                 Integer created_by,
                 Integer updated_by) {
        this.code = code;
        this.description = description;
        this.category_id = category_id;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

}
