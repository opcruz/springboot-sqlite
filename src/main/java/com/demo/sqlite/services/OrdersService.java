package com.demo.sqlite.services;

import com.demo.sqlite.dtos.OrderResultDTO;
import com.demo.sqlite.dtos.ProductOrderDTO;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.repositories.OrderDetailsRepository;
import com.demo.sqlite.repositories.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    private final OrdersRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OrdersService(@Autowired OrdersRepository orderRepository,
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
            double total =
                    products.stream()
                            .mapToDouble(product -> product.getPrice() * product.getQuantity())
                            .sum();
            return OrderResultDTO.builder()
                    .id(value.getId())
                    .paymentMethod(value.getPayment_method())
                    .total(total)
                    .createdAt(value.getCreated_at())
                    .products(products)
                    .build();
        });
    }

}
