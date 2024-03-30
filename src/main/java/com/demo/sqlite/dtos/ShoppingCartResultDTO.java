package com.demo.sqlite.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ShoppingCartResultDTO {

    private Integer clientId;
    private Double total;
    private Iterable<ProductCartDTO> products;

}
