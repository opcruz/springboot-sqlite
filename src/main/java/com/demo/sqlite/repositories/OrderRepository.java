package com.demo.sqlite.repositories;

import com.demo.sqlite.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query(value = "SELECT u FROM orders u WHERE u.client_id = :client_id")
    List<Order> findByClientId(@Param("client_id") Integer clientId);

    @Query(value = "SELECT u FROM orders u WHERE u.id = :id AND u.client_id = :client_id ")
    Optional<Order> findByIdAndClientId(@Param("client_id") Integer clientId, @Param("id") Integer id);

}
