package com.demo.sqlite.dtos;

import com.demo.sqlite.models.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductOrderDTO {

    private Integer quantity;
    private Double price;
    private Stock stock;

    public ProductOrderDTO(Integer quantity,
                           Double price,
                           Integer code,
                           String description,
                           String color,
                           String category,
                           String status) {
        this.quantity = quantity;
        this.price = price;
        this.stock =
                Stock.builder()
                        .code(code)
                        .description(description)
                        .color(color)
                        .category(category)
                        .status(status)
                        .build();
    }

}
