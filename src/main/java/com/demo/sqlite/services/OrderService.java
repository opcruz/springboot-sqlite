package com.demo.sqlite.services;

import com.demo.sqlite.dtos.OrderResultDTO;
import com.demo.sqlite.dtos.ProductOrderDTO;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.repositories.OrderDetailsRepository;
import com.demo.sqlite.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderService(@Autowired OrderRepository orderRepository,
                        @Autowired OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public List<Order> findByClientId(int clientId) {
        return orderRepository.findByClientId(clientId);
    }


    public Optional<OrderResultDTO> orderDetails(int clientId, int orderId) {
        Optional<Order> order = orderRepository.findByIdAndClientId(clientId, orderId);
        return order.map(value -> {
            List<ProductOrderDTO> products = orderDetailsRepository.findByOrderId(value.getId());
            return OrderResultDTO.builder()
                    .id(value.getId())
                    .status(value.getStatus())
                    .paymentMethod(value.getPayment_method())
                    .total(value.getTotal())
                    .createdAt(value.getCreated_at())
                    .products(products)
                    .build();
        });
    }

}
