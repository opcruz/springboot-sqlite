package com.demo.sqlite.dtos;

import com.demo.sqlite.models.Category;
import com.demo.sqlite.models.Stock;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StockResponseDTO {
    private int code;
    private String description;
    private int quantity;
    private double price;
    private Category category;
    private String status;
    private int created_by;
    private int updated_by;

    public StockResponseDTO(Integer code,
                            String description,
                            int category_id,
                            String category_name,
                            String category_description,
                            Integer quantity,
                            Double price,
                            String status,
                            Integer created_by,
                            Integer updated_by) {
        this.code = code;
        this.description = description;
        this.category = new Category(category_id, category_name, category_description);
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.created_by = created_by;
        this.updated_by = updated_by;
    }

    public static StockResponseDTO from(Stock stock, Category category) {
        return new StockResponseDTO(
                stock.getCode(),
                stock.getDescription(),
                category.getId(),
                category.getCategory(),
                category.getDescription(),
                stock.getQuantity(),
                stock.getPrice(),
                stock.getStatus(),
                stock.getCreated_by(),
                stock.getUpdated_by()
        );
    }

}
