package com.demo.sqlite.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class OrderResultResponseDTO {

    private Integer id;
    private String paymentMethod;
    private Double total;
    private Timestamp createdAt;
    private List<ProductOrderDTO> products;

}
