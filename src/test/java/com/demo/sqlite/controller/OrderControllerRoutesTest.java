package com.demo.sqlite.controller;

import com.demo.sqlite.model.dto.ProductOrderDTO;
import com.demo.sqlite.model.dto.response.OrderResultResponseDTO;
import com.demo.sqlite.model.entity.Order;
import com.demo.sqlite.security.JWTCoder;
import com.demo.sqlite.service.OrderService;
import com.demo.sqlite.utils.PaymentMethods;
import com.demo.sqlite.utils.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerRoutesTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    int clientUserId = 1;

    String email = "bLW7H@example.com";


    @Test
    void listOrdersWithoutAuth() throws Exception {
        // test
        ResultActions response = mockMvc.perform(get("/orders"));
        // verify
        response.andExpect(status().isForbidden());
        verifyNoInteractions(orderService);
    }


    @Test
    void listOrdersWithClientRoleAuth() throws Exception {
        String token = JWTCoder.generateJWT(email, Collections.singletonList(Roles.CLIENT.getRoleWithPrefix()), clientUserId);

        // mock
        Order newOrder = Order.builder()
                .paymentMethod(PaymentMethods.VISA.getValue())
                .clientId(clientUserId)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        List<Order> expectedOrders = List.of(newOrder);
        when(orderService.findByClientId(clientUserId)).thenReturn(expectedOrders);

        // test
        ResultActions response = mockMvc.perform(
                get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(newOrder.getId())))
                .andExpect(jsonPath("$[0].clientId", is(newOrder.getClientId())))
                .andExpect(jsonPath("$[0].paymentMethod", is(newOrder.getPaymentMethod())));

        // verify
        verify(orderService, times(1)).findByClientId(clientUserId);
    }

    @Test
    void listOrdersWithEmployeeRoleAuth() throws Exception {
        String token = JWTCoder.generateJWT(email, Collections.singletonList(Roles.EMPLOYEE.getRoleWithPrefix()), clientUserId);

        // test
        ResultActions response = mockMvc.perform(
                get("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );
        // verify
        response.andExpect(status().isForbidden());
        verifyNoInteractions(orderService);
    }

    @Test
    void orderDetailsWithoutAuth() throws Exception {
        int orderId = 56;
        // test
        ResultActions response = mockMvc.perform(get("/orders/{orderId}/details", orderId));
        // verify
        response.andExpect(status().isForbidden());
        verifyNoInteractions(orderService);
    }

    @Test
    void orderDetailsWithClientRoleAuth() throws Exception {
        int orderId = 56;
        String token = JWTCoder.generateJWT(email, Collections.singletonList(Roles.CLIENT.getRoleWithPrefix()), clientUserId);

        List<ProductOrderDTO> productOrderDTOS = List.of(
                new ProductOrderDTO(1, 1500.0, 1, "coca", 1, "active")
        );

        OrderResultResponseDTO resultResponseDTO =
                OrderResultResponseDTO.builder()
                        .id(orderId)
                        .paymentMethod(PaymentMethods.PAYPAL.getValue())
                        .total(1500.0)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .products(productOrderDTOS)
                        .build();
        // mock
        when(orderService.orderDetails(clientUserId, orderId)).thenReturn(Optional.of(resultResponseDTO));

        // test
        ResultActions response = mockMvc.perform(
                get("/orders/{orderId}/details", orderId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.products", hasSize(1)))
                .andExpect(jsonPath("$.id", is(orderId)))
                .andExpect(jsonPath("$.paymentMethod", is(resultResponseDTO.getPaymentMethod())))
                .andExpect(jsonPath("$.total", is(resultResponseDTO.getTotal())));

        // verify
        verify(orderService, times(1)).orderDetails(clientUserId, orderId);
    }


    @Test
    void orderDetailsWithEmployeeRoleAuth() throws Exception {
        int orderId = 56;
        String token = JWTCoder.generateJWT(email, Collections.singletonList(Roles.EMPLOYEE.getRoleWithPrefix()), clientUserId);
        // test
        ResultActions response = mockMvc.perform(
                get("/orders/{orderId}/details", orderId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        );

        // verify
        response.andExpect(status().isForbidden());
        verifyNoInteractions(orderService);
    }

}
