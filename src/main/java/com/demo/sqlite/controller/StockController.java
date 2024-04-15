package com.demo.sqlite.controller;

import com.demo.sqlite.model.dto.response.StockResponseDTO;
import com.demo.sqlite.security.UserAuthenticateInfo;
import com.demo.sqlite.service.StockService;
import com.demo.sqlite.utils.Try;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(@Autowired StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(path = "/list")
    @Operation(summary = "List products")
    public @ResponseBody List<StockResponseDTO> getAllStocks(
            @RequestParam(required = false) String searchPhrase,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pagination = PageRequest.of(page, size);
        return stockService.getAllStocks(searchPhrase, pagination);
    }

    @GetMapping(value = "/{code}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @Operation(summary = "Return picture")
    public ResponseEntity<byte[]> getImage(@PathVariable int code) {
        return stockService.findImageByCode(code)
                .map(bytes -> ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Add stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public @ResponseBody ResponseEntity<StockResponseDTO> createStock(
            @Parameter(
                    name = "description",
                    description = "Descripción del producto")
            @RequestPart String description,
            @Parameter(
                    name = "category",
                    description = "Categoría del producto",
                    schema = @Schema(type = "integer", format = "int32",
                            allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
                            description = "1: Clothing and Accessories<br> 2: Consumer Electronics<br> 3: Beauty and Personal Care<br> 4: Home and Kitchen<br> 5: Books<br> 6: Toys and Games<br> 7: Sports and Outdoor Activities<br> 8: Health and Wellness<br> 9: Automotive<br> 10: Food and Beverages"
                    ))
            @RequestPart String category,
            @Parameter(
                    name = "price",
                    description = "Precio del producto",
                    schema = @Schema(type = "number", format = "double"))
            @RequestPart String price,
            @Parameter(
                    name = "quantity",
                    description = "Cantidad del producto",
                    schema = @Schema(type = "integer", format = "int32"))
            @RequestPart String quantity,
            @Parameter(
                    name = "status",
                    description = "Estatus del producto",
                    schema = @Schema(type = "string", allowableValues = {"active", "inactive", "out_of_stock", "deleted"}))
            @RequestPart String status,
            @Parameter(
                    name = "image",
                    description = "Imagen del producto")
            @RequestPart(required = false) MultipartFile image,
            Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        Optional<byte[]> bytesImageOpt = Optional.ofNullable(image).flatMap(value -> Try.of(value::getBytes).toOptional());
        StockResponseDTO stockResponseDTO = stockService.addProduct(description, Integer.parseInt(category), status,
                Double.parseDouble(price), Integer.parseInt(quantity), clientId, bytesImageOpt);
        return ResponseEntity.ok().body(stockResponseDTO);
    }

    @PutMapping(path = "/{code}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<StockResponseDTO> updateStock(
            @PathVariable int code,
            @Parameter(
                    name = "description",
                    description = "Descripción del producto")
            @RequestPart String description,
            @Parameter(
                    name = "category",
                    description = "Categoría del producto",
                    schema = @Schema(type = "integer", format = "int32",
                            allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
                            description = "1: Clothing and Accessories<br> 2: Consumer Electronics<br> 3: Beauty and Personal Care<br> 4: Home and Kitchen<br> 5: Books<br> 6: Toys and Games<br> 7: Sports and Outdoor Activities<br> 8: Health and Wellness<br> 9: Automotive<br> 10: Food and Beverages"
                    ))
            @RequestPart String category,
            @Parameter(
                    name = "price",
                    description = "Precio del producto",
                    schema = @Schema(type = "number", format = "double"))
            @RequestPart String price,
            @Parameter(
                    name = "quantity",
                    description = "Cantidad del producto",
                    schema = @Schema(type = "integer", format = "int32"))
            @RequestPart String quantity,
            @Parameter(
                    name = "status",
                    description = "Estatus del producto",
                    schema = @Schema(type = "string", allowableValues = {"active", "inactive", "out_of_stock", "deleted"}))
            @RequestPart String status,
            @Parameter(
                    name = "image",
                    description = "Imagen del producto")
            @RequestPart(required = false) MultipartFile image,
            Authentication auth) {
        int clientId = UserAuthenticateInfo.fromAuth(auth).getUserId();
        Optional<byte[]> bytesImageOpt = Optional.ofNullable(image).flatMap(value -> Try.of(value::getBytes).toOptional());
        return stockService.updateProduct(
                        code, description, Integer.parseInt(category), status,
                        Double.parseDouble(price), Integer.parseInt(quantity), clientId, bytesImageOpt)
                .map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{code}")
    @Operation(summary = "Get stock product")
    public @ResponseBody ResponseEntity<StockResponseDTO> getStock(@PathVariable int code) {
        return stockService.findStockByCode(code).map(result -> ResponseEntity.ok().body(result))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{code}")
    @Operation(summary = "Delete stock product", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> deleteStock(@PathVariable int code) {
        if (stockService.deleteProduct(code)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
