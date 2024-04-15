package com.demo.sqlite.model.dto.response;

import com.demo.sqlite.model.dto.ProductCartDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ShoppingCartResultResponseDTO {

    private Integer clientId;
    private Double total;
    private Iterable<ProductCartDTO> products;

}
