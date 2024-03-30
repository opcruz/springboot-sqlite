package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.OrderResultDTO;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.repositories.OrderRepository;
import com.demo.sqlite.security.UserAuthenticateInfo;
import com.demo.sqlite.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;


    public OrderController(@Autowired OrderRepository orderRepository,
                           @Autowired OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List orders", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody List<Order> getShoppingCart(Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        return orderRepository.findByClientId(clientId);
    }

    @GetMapping(path = "/{order_id}/details")
    @Operation(summary = "List orders", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ResponseEntity<OrderResultDTO> orderDetails(@PathVariable(value = "order_id") Integer orderId,
                                                                     Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        return orderService.orderDetails(clientId, orderId)
                .map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
    }

}
