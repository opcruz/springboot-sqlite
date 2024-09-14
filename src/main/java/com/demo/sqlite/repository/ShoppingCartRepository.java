package com.demo.sqlite.repository;

import com.demo.sqlite.model.dto.ShoppingCartJoined;
import com.demo.sqlite.model.entity.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer> {

   @Query(value = "SELECT new com.demo.sqlite.model.dto.ShoppingCartJoined(u.id, u.quantity,"
         + " s.code, s.description, s.quantity, s.categoryId, s.price, s.status)"
         + " FROM shopping_cart u JOIN stock s ON u.productCode = s.code WHERE u.clientId = :client_id")
   List<ShoppingCartJoined> filterByClientId(@Param("client_id") int clientId);

   @Query(value = "SELECT u FROM shopping_cart u WHERE u.clientId = :client_id AND u.productCode = :stock_code")
   Optional<ShoppingCart> filterByClientIdAndStockCode(@Param("client_id") int clientId,
         @Param("stock_code") int stockCode);

   @Modifying
   @Query(value = "DELETE FROM shopping_cart u WHERE u.id = :cart_id AND u.clientId = :client_id")
   Integer deleteByIdAndClientId(@Param("cart_id") int cartId, @Param("client_id") int clientId);

}
