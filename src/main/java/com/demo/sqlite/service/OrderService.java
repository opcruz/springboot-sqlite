package com.demo.sqlite.service;

import com.demo.sqlite.model.dto.response.OrderResultResponseDTO;
import com.demo.sqlite.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findByClientId(int clientId);

    Optional<OrderResultResponseDTO> orderDetails(int clientId, int orderId);


}

