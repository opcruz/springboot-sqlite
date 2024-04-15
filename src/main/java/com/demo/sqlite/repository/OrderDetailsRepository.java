package com.demo.sqlite.repository;

import com.demo.sqlite.model.dto.ProductOrderDTO;
import com.demo.sqlite.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {

    @Query(value = "SELECT new com.demo.sqlite.model.dto.ProductOrderDTO(u.quantity, u.price, s.code, s.description, s.categoryId, s.status)" +
            " FROM order_details u JOIN stock s ON u.productCode = s.code WHERE u.orderId = :order_id")
    List<ProductOrderDTO> findByOrderId(@Param("order_id") Integer order_id);

}
