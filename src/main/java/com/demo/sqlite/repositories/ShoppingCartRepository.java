package com.demo.sqlite.repositories;

import com.demo.sqlite.dtos.ShoppingCartJoined;
import com.demo.sqlite.models.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer> {

    @Query(value = "SELECT new com.demo.sqlite.dtos.ShoppingCartJoined(u.id, u.quantity," +
            " s.code, s.description, s.color, s.category, s.price, s.status)" +
            " FROM shopping_cart u JOIN stock s ON u.product_code = s.code WHERE u.client_id = :client_id")
    Iterable<ShoppingCartJoined> filterByClientId(@Param("client_id") Integer clientId);

    @Transactional
    @Modifying
    @Query(value = "delete from shopping_cart u where u.id = :cart_id AND u.client_id = :client_id")
    Integer deleteByIdAndClientId(@Param("cart_id") Integer cartId, @Param("client_id") Integer clientId);

}
