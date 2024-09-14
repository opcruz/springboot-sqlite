package com.demo.sqlite.service;

import com.demo.sqlite.model.dto.response.ShoppingCartResultResponseDTO;
import com.demo.sqlite.model.entity.Order;
import com.demo.sqlite.model.entity.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartService {

   ShoppingCartResultResponseDTO getShoppingCart(int clientId);

   public ShoppingCart addCartProduct(int clientId, int productCode, int quantity);

   boolean deleteStockFromCart(int cartId, int clientId);

   Order buyCart(int clientId, String paymentMethod);

}
