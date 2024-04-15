package com.demo.sqlite.model.dto;

import com.demo.sqlite.model.entity.Stock;
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
                        .categoryId(category)
                        .price(price)
                        .status(status)
                        .build();
    }

}
