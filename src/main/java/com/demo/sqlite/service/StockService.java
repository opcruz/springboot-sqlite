package com.demo.sqlite.service;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.response.StockResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StockService {

    List<StockResponseDTO> getAllStocks(String searchPhrase, Pageable pagination);

    Optional<byte[]> findImageByCode(int code);

    StockResponseDTO addProduct(
            String description, int categoryId, String status,
            double price, int quantity, int clientId,
            Optional<byte[]> imageOpt) throws ValidationError;

    Optional<StockResponseDTO> updateProduct(
            int code, String description, int categoryId, String status,
            double price, int quantity, int clientId, Optional<byte[]> imageOpt) throws ValidationError;

    Optional<StockResponseDTO> findStockByCode(int code);

    boolean deleteProduct(int code);

}
