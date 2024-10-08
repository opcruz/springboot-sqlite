package com.demo.sqlite.model.dto;

import com.demo.sqlite.model.entity.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ProductCartDTO {

   private Integer cartId;
   private Integer quantity;
   private Stock stock;

}
