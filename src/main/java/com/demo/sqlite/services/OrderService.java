package com.demo.sqlite.services;

import com.demo.sqlite.dtos.OrderResultDTO;
import com.demo.sqlite.dtos.ProductOrderDTO;
import com.demo.sqlite.dtos.ShoppingCartJoined;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.models.OrderDetails;
import com.demo.sqlite.repositories.OrderDetailsRepository;
import com.demo.sqlite.repositories.OrderRepository;
import com.demo.sqlite.repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailsRepository orderDetailsRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    public OrderService(@Autowired OrderRepository orderRepository,
                        @Autowired OrderDetailsRepository orderDetailsRepository,
                        @Autowired ShoppingCartRepository shoppingCartRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.shoppingCartRepository = shoppingCartRepository;
    }


    @Transactional(rollbackFor = {SQLException.class})
    public Order generateOrder(int clientId, String payment_method) {
        Iterable<ShoppingCartJoined> shoppingCartJList = shoppingCartRepository.filterByClientId(clientId);

        double total = 0.0f;
        LinkedList<Integer> deletedCartIds = new LinkedList<>();
        for (ShoppingCartJoined c : shoppingCartJList) {
            total += c.getQuantity() * c.getStock().getPrice();
            deletedCartIds.add(c.getId());
        }

        Order o = new Order();
        o.setCreated_at(new Timestamp(System.currentTimeMillis()));
        o.setStatus("pagado");
        o.setPayment_method(payment_method);
        o.setTotal(total);
        o.setClient_id(clientId);
        Order orderSaved = orderRepository.save(o);

        LinkedList<OrderDetails> list = new LinkedList<>();
        for (ShoppingCartJoined cart : shoppingCartJList) {
            OrderDetails details =
                    OrderDetails.builder()
                            .order_id(orderSaved.getId())
                            .product_code(cart.getStock().getCode())
                            .quantity(cart.getQuantity())
                            .price(cart.getStock().getPrice())
                            .build();
            list.add(details);
        }
        orderDetailsRepository.saveAll(list);
        shoppingCartRepository.deleteAllById(deletedCartIds);
        return orderSaved;
    }

    public Optional<OrderResultDTO> orderDetails(int clientId, int orderId) {
        Optional<Order> order = orderRepository.findByIdAndClientId(clientId, orderId);
        return order.map(value -> {
            Iterable<ProductOrderDTO> products = orderDetailsRepository.findByOrderId(value.getId());
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
