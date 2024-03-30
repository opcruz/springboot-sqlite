package com.demo.sqlite.dtos;

import com.demo.sqlite.models.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductOrderDTO {

    private int quantity;
    private double price;
    private Stock stock;

    public ProductOrderDTO(Integer quantity,
                           Double price,
                           Integer code,
                           String description,
                           int category,
                           String status) {
        this.quantity = quantity;
        this.price = price;
        this.stock =
                Stock.builder()
                        .code(code)
                        .description(description)
                        .category_id(category)
                        .status(status)
                        .build();
    }

}
