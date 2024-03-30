package com.demo.sqlite.controllers;

import com.demo.sqlite.models.Stock;
import com.demo.sqlite.repositories.StockRepository;
import com.demo.sqlite.security.UserAuthenticateInfo;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StockController {
    private final StockRepository stockRepository;

    public StockController(@Autowired StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List products")
    public @ResponseBody List<Stock> getAllStocks(@RequestParam(required = false) String searchPhrase) {
        List<Stock> result;
        if (searchPhrase == null || searchPhrase.isBlank()) {
            result = stockRepository.allWithoutImage();
        } else {
            result = stockRepository.findByName(searchPhrase);
        }
        return result;
    }

    @GetMapping(value = "/{code}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Return picture")
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
                                           @RequestPart String category,
                                           @RequestPart String price,
                                           @RequestPart String quantity,
                                           @RequestPart String status,
                                           @RequestPart(required = false) MultipartFile image,
                                           Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        Stock newStock =
                Stock.builder()
                        .description(description)
                        .category_id(Integer.parseInt(category))
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
                                             @RequestPart int category,
                                             @RequestPart String price,
                                             @RequestPart String quantity,
                                             @RequestPart String status,
                                             @RequestPart(required = false) MultipartFile image,
                                             Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        return
                stockRepository.findById(code).map(stock -> {
                    stock.setCategory_id(category);
                    stock.setPrice(Double.parseDouble(price));
                    stock.setDescription(description);
                    stock.setQuantity(Integer.parseInt(quantity));
                    stock.setStatus(status);
                    stock.setUpdated_by(clientId);

                    if (image != null) {
                        try {
                            stock.setImage(image.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    stockRepository.save(stock);
                    return stock;
                }).map(result -> ResponseEntity.ok().body(result)
                ).orElse(ResponseEntity.notFound().build());

    }

    @GetMapping(path = "/{code}")
    @Operation(summary = "Get stock product")
    public @ResponseBody ResponseEntity<Stock> getStock(@PathVariable int code) {
        return stockRepository.findById(code).map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
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
