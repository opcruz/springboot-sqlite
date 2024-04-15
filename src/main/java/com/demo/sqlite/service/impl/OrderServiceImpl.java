package com.demo.sqlite.service.impl;

import com.demo.sqlite.model.dto.ProductOrderDTO;
import com.demo.sqlite.model.dto.response.OrderResultResponseDTO;
import com.demo.sqlite.model.entity.Order;
import com.demo.sqlite.repository.OrderDetailsRepository;
import com.demo.sqlite.repository.OrdersRepository;
import com.demo.sqlite.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrderServiceImpl(@Autowired OrdersRepository orderRepository,
                            @Autowired OrderDetailsRepository orderDetailsRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public List<Order> findByClientId(int clientId) {
        return orderRepository.findByClientId(clientId);
    }

    @Override
    public Optional<OrderResultResponseDTO> orderDetails(int clientId, int orderId) {
        Optional<Order> order = orderRepository.findByIdAndClientId(clientId, orderId);
        return order.map(value -> {
            List<ProductOrderDTO> products = orderDetailsRepository.findByOrderId(value.getId());
            double total =
                    products.stream()
                            .mapToDouble(product -> product.getPrice() * product.getQuantity())
                            .sum();
            return OrderResultResponseDTO.builder()
                    .id(value.getId())
                    .paymentMethod(value.getPaymentMethod())
                    .total(total)
                    .createdAt(value.getCreatedAt())
                    .products(products)
                    .build();
        });
    }

}
