package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.ShoppingCartResultDTO;
import com.demo.sqlite.exceptions.ValidationError;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.models.ShoppingCart;
import com.demo.sqlite.security.UserAuthenticateInfo;
import com.demo.sqlite.services.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(@Autowired ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    @Operation(summary = "List cart products", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ResponseEntity<ShoppingCartResultDTO> getShoppingCart(Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        ShoppingCartResultDTO shoppingCart = shoppingCartService.getShoppingCart(clientId);
        return ResponseEntity.ok().body(shoppingCart);
    }

    @DeleteMapping(path = "/{cartId}")
    @Operation(summary = "Delete cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStockFromCart(@PathVariable Integer cartId,
                                                    Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        if (shoppingCartService.deleteStockFromCart(cartId, clientId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ShoppingCart> addCartProduct(@RequestParam Integer productCode,
                                                       @RequestParam Integer quantity,
                                                       Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        return shoppingCartService.addCartProduct(clientId, productCode, quantity)
                .map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/buy")
    @Operation(summary = "Buy cart", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ResponseEntity<Order> buyCart(
            @Parameter(
                    name = "payment_method",
                    description = "Payment Method",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", allowableValues = {"CASH", "VISA", "MASTERCARD", "PAYPAL"}),
                    example = "CASH")
            @RequestParam(name = "payment_method") String paymentMethod,
            Authentication auth
    ) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        try {
            Order order = shoppingCartService.buyCart(clientId, paymentMethod);
            return ResponseEntity.ok().body(order);
        } catch (
                ValidationError e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
