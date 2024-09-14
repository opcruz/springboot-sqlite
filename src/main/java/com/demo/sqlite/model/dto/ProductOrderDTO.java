package com.demo.sqlite.model.dto;

import com.demo.sqlite.model.entity.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductOrderDTO {

   private int quantity;
   private double price;
   private Stock stock;

   public ProductOrderDTO(Integer quantity, Double price, Integer code, String description,
         int category, String status) {
      this.quantity = quantity;
      this.price = price;
      this.stock = Stock.builder().code(code).description(description).categoryId(category)
            .status(status).build();
   }

}
