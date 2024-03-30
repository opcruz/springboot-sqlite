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

    public ShoppingCartJoined(int id, int quantity,
                              Integer code, String description,
                              int category, Double price,
                              String status) {

        this.id = id;
        this.quantity = quantity;
        this.stock =
                Stock.builder()
                        .code(code)
                        .description(description)
                        .category_id(category)
                        .price(price)
                        .status(status)
                        .build();
    }

}
