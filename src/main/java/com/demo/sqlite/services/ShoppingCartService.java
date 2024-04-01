package com.demo.sqlite.services;

import com.demo.sqlite.dtos.ProductCartDTO;
import com.demo.sqlite.dtos.ShoppingCartJoined;
import com.demo.sqlite.dtos.ShoppingCartResultDTO;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.models.OrderDetails;
import com.demo.sqlite.models.ShoppingCart;
import com.demo.sqlite.repositories.OrderDetailsRepository;
import com.demo.sqlite.repositories.OrderRepository;
import com.demo.sqlite.repositories.ShoppingCartRepository;
import com.demo.sqlite.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class ShoppingCartService {
    private final StockRepository stockRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public ShoppingCartService(@Autowired StockRepository stockRepository,
                               @Autowired ShoppingCartRepository shoppingCartRepository,
                               @Autowired OrderRepository orderRepository,
                               @Autowired OrderDetailsRepository orderDetailsRepository) {
        this.stockRepository = stockRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    public ShoppingCartResultDTO getShoppingCart(int clientId) {
        Iterable<ShoppingCartJoined> shoppingCarts = shoppingCartRepository.filterByClientId(clientId);

        ShoppingCartResultDTO.ShoppingCartResultDTOBuilder builder = ShoppingCartResultDTO.builder();
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
                                .product_code(productCode)
                                .quantity(quantity)
                                .client_id(clientId)
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
    public Order buyCart(int clientId, String payment_method) {
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

}
