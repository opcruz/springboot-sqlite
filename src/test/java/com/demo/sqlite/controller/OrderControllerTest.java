package com.demo.sqlite.controller;

import com.demo.sqlite.model.dto.response.OrderResultResponseDTO;
import com.demo.sqlite.model.entity.Order;
import com.demo.sqlite.security.UserAuthenticateInfo;
import com.demo.sqlite.service.OrderService;
import com.demo.sqlite.utils.PaymentMethods;
import com.demo.sqlite.utils.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
   @Mock
   private OrderService orderService;

   @InjectMocks
   private OrderController orderController;
   int clientUserId = 1;
   private UsernamePasswordAuthenticationToken clientAuthentication;

   @BeforeEach
   void setUp() {
      clientAuthentication = new UsernamePasswordAuthenticationToken("email@example", null,
            List.of(new SimpleGrantedAuthority(Roles.CLIENT.getRoleWithPrefix())));
      UserAuthenticateInfo userAuthenticateInfoClient = UserAuthenticateInfo.builder().userId(clientUserId)
            .subject("email@example")
            .roles(List.of(Roles.CLIENT.getRoleWithPrefix())).build();
      clientAuthentication.setDetails(userAuthenticateInfoClient);
   }

   @Test
   void testListOrders() {
      Order newOrder = Order.builder().paymentMethod(PaymentMethods.VISA.getValue())
            .clientId(clientUserId).createdAt(new Timestamp(System.currentTimeMillis())).build();
      List<Order> expectedOrders = List.of(newOrder);
      when(orderService.findByClientId(clientUserId)).thenReturn(expectedOrders);

      // Test
      List<Order> result = orderController.listOrders(clientAuthentication);

      // Assertions
      assertEquals(expectedOrders, result);
      verify(orderService, times(1)).findByClientId(eq(clientUserId));
   }

   @Test
   void testOrderDetailsExistingOrder() {
      int orderId = 123;
      double total = 1500;
      OrderResultResponseDTO expectedResult = OrderResultResponseDTO.builder().id(orderId)
            .paymentMethod(PaymentMethods.PAYPAL.getValue()).total(total)
            .createdAt(new Timestamp(System.currentTimeMillis())).products(List.of()).build();
      when(orderService.orderDetails(clientUserId, orderId))
            .thenReturn(Optional.of(expectedResult));

      // Test
      ResponseEntity<OrderResultResponseDTO> response = orderController.orderDetails(orderId, clientAuthentication);

      // Assertions
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(expectedResult, response.getBody());
      verify(orderService, times(1)).orderDetails(clientUserId, orderId);
   }

   @Test
   void testOrderDetailsNonExistingOrder() {
      int orderId = 123;
      when(orderService.orderDetails(clientUserId, orderId)).thenReturn(Optional.empty());

      // Test
      ResponseEntity<OrderResultResponseDTO> response = orderController.orderDetails(orderId, clientAuthentication);

      // Assertions
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      verify(orderService, times(1)).orderDetails(clientUserId, orderId);
   }

}
