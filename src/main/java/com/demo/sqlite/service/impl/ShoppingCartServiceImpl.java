package com.demo.sqlite.service.impl;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.ProductCartDTO;
import com.demo.sqlite.model.dto.ShoppingCartJoined;
import com.demo.sqlite.model.dto.response.ShoppingCartResultResponseDTO;
import com.demo.sqlite.model.entity.Order;
import com.demo.sqlite.model.entity.OrderDetails;
import com.demo.sqlite.model.entity.ShoppingCart;
import com.demo.sqlite.repository.OrderDetailsRepository;
import com.demo.sqlite.repository.OrdersRepository;
import com.demo.sqlite.repository.ShoppingCartRepository;
import com.demo.sqlite.repository.StockRepository;
import com.demo.sqlite.service.ShoppingCartService;
import com.demo.sqlite.utils.PaymentMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final StockRepository stockRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrdersRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public ShoppingCartServiceImpl(@Autowired StockRepository stockRepository,
                                   @Autowired ShoppingCartRepository shoppingCartRepository,
                                   @Autowired OrdersRepository orderRepository,
                                   @Autowired OrderDetailsRepository orderDetailsRepository) {
        this.stockRepository = stockRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public ShoppingCartResultResponseDTO getShoppingCart(int clientId) {
        Iterable<ShoppingCartJoined> shoppingCarts = shoppingCartRepository.filterByClientId(clientId);

        ShoppingCartResultResponseDTO.ShoppingCartResultResponseDTOBuilder builder = ShoppingCartResultResponseDTO.builder();
        builder.clientId(clientId);

        double total = 0.0;
        LinkedList<ProductCartDTO> productsCart = new LinkedList<>();
        for (ShoppingCartJoined productCart : shoppingCarts) {
            total += productCart.getQuantity() * productCart.getStock().getPrice();
            productsCart.add(
                    ProductCartDTO.builder()
                            .cartId(productCart.getId())
                            .quantity(productCart.getQuantity())
                            .stock(productCart.getStock())
                            .build()
            );
        }
        builder.total(total);
        builder.products(productsCart);
        return builder.build();
    }

    public Optional<ShoppingCart> addCartProduct(int clientId, int productCode, int quantity) {
        return stockRepository.findById(productCode).flatMap(product -> {
            if (product.getQuantity() >= quantity) {
                ShoppingCart saved = shoppingCartRepository.save(
                        ShoppingCart.builder()
                                .productCode(productCode)
                                .quantity(quantity)
                                .clientId(clientId)
                                .build()
                );
                return Optional.of(saved);
            } else {
                return Optional.empty();
            }
        });
    }

    public boolean deleteStockFromCart(int cartId, int clientId) {
        try {
            shoppingCartRepository.deleteByIdAndClientId(cartId, clientId);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public Order buyCart(int clientId, String paymentMethod) {
        if (!PaymentMethods.isValid(paymentMethod)) {
            throw new ValidationError(String.format("Invalid payment method: %s", paymentMethod));
        }
        List<ShoppingCartJoined> shoppingCartJList = shoppingCartRepository.filterByClientId(clientId);
        List<Integer> shoppingCartIds =
                shoppingCartJList.stream().map(ShoppingCartJoined::getId).toList();

        Order newOrder = Order.builder()
                .paymentMethod(paymentMethod)
                .clientId(clientId)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        Order orderSaved = orderRepository.save(newOrder);
        List<OrderDetails> orderDetailsList = shoppingCartJList.stream().map(cart ->
                OrderDetails.builder()
                        .orderId(orderSaved.getId())
                        .productCode(cart.getStock().getCode())
                        .quantity(cart.getQuantity())
                        .price(cart.getStock().getPrice())
                        .build()
        ).toList();
        orderDetailsRepository.saveAll(orderDetailsList);
        shoppingCartRepository.deleteAllById(shoppingCartIds);
        return orderSaved;
    }

}
