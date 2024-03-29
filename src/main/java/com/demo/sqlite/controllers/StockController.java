package com.demo.sqlite.controllers;

import com.demo.sqlite.models.Stock;
import com.demo.sqlite.repositories.StockRepository;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StockController {
    private final StockRepository stockRepository;

    public StockController(@Autowired StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List products", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Iterable<Stock> getAllStocks(@RequestParam(required = false) String searchPhrase, Authentication authentication) {
        Iterable<Stock> result;
        if (searchPhrase == null || searchPhrase.isBlank()) {
            result = stockRepository.allWithoutImage();
        } else {
            result = stockRepository.findByName(searchPhrase);
        }
        return result;
    }

    @GetMapping(value = "/{code}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Return picture", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<byte[]> getImage(@PathVariable int code) {
        Optional<byte[]> imageByCode = stockRepository.findImageByCode(code);

        return imageByCode.map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(bytes))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Add stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Stock createStock(@RequestPart String description,
                                           @RequestPart(required = false) String color,
                                           @RequestPart String category,
                                           @RequestPart String price,
                                           @RequestPart String quantity,
                                           @RequestPart String status,
                                           @RequestPart(required = false) MultipartFile image,
                                           Authentication auth) {
        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);
        Stock newStock =
                Stock.builder()
                        .description(description)
                        .color(color)
                        .category(category)
                        .status(status)
                        .price(Double.parseDouble(price))
                        .quantity(Integer.parseInt(quantity))
                        .updated_by(clientId)
                        .created_by(clientId)
                        .build();

        if (image != null) {
            try {
                newStock.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return stockRepository.save(newStock);
    }

    @PutMapping(path = "/{code}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Stock> updateStock(@PathVariable int code,
                                             @RequestPart String description,
                                             @RequestPart(required = false) String color,
                                             @RequestPart String category,
                                             @RequestPart String price,
                                             @RequestPart String quantity,
                                             @RequestPart String status,
                                             @RequestPart(required = false) MultipartFile image,
                                             Authentication auth) {

        Integer clientId = ((Claims) auth.getDetails()).get("userId", Integer.class);

        return
                stockRepository.findById(code).map(stock -> {
                    stock.setCategory(category);
                    stock.setColor(color);
                    stock.setPrice(Double.parseDouble(price));
                    stock.setDescription(description);
                    stock.setQuantity(Integer.parseInt(quantity));
                    stock.setStatus(status);
                    stock.setUpdated_by(clientId);
                    stockRepository.save(stock);
                    return stock;
                }).map(result -> ResponseEntity.ok().body(result)
                ).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "/{code}")
    @Operation(summary = "Get stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody Optional<Stock> getStock(@PathVariable int code) {
        return stockRepository.findById(code);
    }

    @DeleteMapping(path = "/{code}")
    @Operation(summary = "Delete stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteStock(@PathVariable int code) {
        try {
            stockRepository.deleteById(code);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
