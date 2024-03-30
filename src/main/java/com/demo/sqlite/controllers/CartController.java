package com.demo.sqlite.controllers;

import com.demo.sqlite.dtos.ProductCartDTO;
import com.demo.sqlite.dtos.ShoppingCartJoined;
import com.demo.sqlite.dtos.ShoppingCartResultDTO;
import com.demo.sqlite.models.Order;
import com.demo.sqlite.models.ShoppingCart;
import com.demo.sqlite.repositories.ShoppingCartRepository;
import com.demo.sqlite.repositories.StockRepository;
import com.demo.sqlite.security.UserAuthenticateInfo;
import com.demo.sqlite.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping(value = "/carts")
public class CartController {
    private final ShoppingCartRepository shoppingCartRepository;
    private final StockRepository stockRepository;
    private final OrderService orderService;


    public CartController(@Autowired ShoppingCartRepository shoppingCartRepository,
                          @Autowired OrderService orderService,
                          @Autowired StockRepository stockRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderService = orderService;
        this.stockRepository = stockRepository;
    }

    @GetMapping
    @Operation(summary = "List cart products", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ShoppingCartResultDTO getShoppingCart(Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
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

    @DeleteMapping(path = "/{cartId}")
    @Operation(summary = "Delete cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStock(@RequestParam Integer cartId,
                                            Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        try {
            shoppingCartRepository.deleteByIdAndClientId(cartId, clientId);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Add cart product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ShoppingCart> addCartProduct(@RequestParam Integer productCode,
                                                       @RequestParam Integer quantity,
                                                       Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();

        ShoppingCart shoppingCartProduct = new ShoppingCart();
        shoppingCartProduct.setProduct_code(productCode);
        shoppingCartProduct.setQuantity(quantity);
        shoppingCartProduct.setClient_id(clientId);

        Optional<ShoppingCart> objectOptional = stockRepository.findById(productCode).flatMap(product -> {
            if (product.getQuantity() >= quantity) {
                ShoppingCart saved = shoppingCartRepository.save(shoppingCartProduct);
                return Optional.of(saved);
            } else {
                return Optional.empty();
            }
        });

        return objectOptional.map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping(path = "/buy")
    @Operation(summary = "Buy cart", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Order buyCart(@RequestParam String payment_method,
                                       Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        return orderService.generateOrder(clientId, payment_method);
    }

}
