package com.demo.sqlite.repositories;

import com.demo.sqlite.dtos.ProductOrderDTO;
import com.demo.sqlite.models.OrderDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {

    @Query(value = "SELECT new com.demo.sqlite.dtos.ProductOrderDTO(u.quantity, u.price, s.code, s.description, s.category_id, s.status)" +
            " FROM order_details u JOIN stock s ON u.product_code = s.code WHERE u.order_id = :order_id")
    List<ProductOrderDTO> findByOrderId(@Param("order_id") Integer order_id);

}
