package com.demo.sqlite.dtos;

import com.demo.sqlite.models.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ShoppingCartJoined {

    private int id;

    private Stock stock;

    private int quantity;

    public ShoppingCartJoined(int id, int quantityCart,
                              Integer code, String description,
                              int quantityStock,
                              int category, Double price,
                              String status) {

        this.id = id;
        this.quantity = quantityCart;
        this.stock =
                Stock.builder()
                        .code(code)
                        .description(description)
                        .quantity(quantityStock)
                        .category_id(category)
                        .price(price)
                        .status(status)
                        .build();
    }

}
