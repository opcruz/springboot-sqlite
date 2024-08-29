package com.demo.sqlite.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "stock")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int code;
   private String description;
   @JsonIgnore
   private byte[] image;
   @Column(name = "category_id")
   private int categoryId;
   private int quantity;
   private double price;
   private String status;
   @Column(name = "created_by")
   private int createdBy;
   @Column(name = "updated_by")
   private int updatedBy;

   public Stock(Integer code,
         String description,
         int categoryId,
         Integer quantity,
         Double price,
         String status,
         int createdBy,
         int updatedBy) {
      this.code = code;
      this.description = description;
      this.categoryId = categoryId;
      this.quantity = quantity;
      this.price = price;
      this.status = status;
      this.createdBy = createdBy;
      this.updatedBy = updatedBy;
   }

}
