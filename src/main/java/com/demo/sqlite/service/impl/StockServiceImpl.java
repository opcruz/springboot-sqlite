package com.demo.sqlite.service.impl;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.response.StockResponseDTO;
import com.demo.sqlite.model.entity.Category;
import com.demo.sqlite.model.entity.Stock;
import com.demo.sqlite.repository.CategoryRepository;
import com.demo.sqlite.repository.StockRepository;
import com.demo.sqlite.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final CategoryRepository categoryRepository;

    public StockServiceImpl(@Autowired StockRepository stockRepository,
                            @Autowired CategoryRepository categoryRepository) {
        this.stockRepository = stockRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<StockResponseDTO> getAllStocks(String searchPhrase, Pageable pagination) {
        return stockRepository.filterByPhraseAndPagination(searchPhrase, pagination).getContent();
    }

    public Optional<byte[]> findImageByCode(int code) {
        return stockRepository.findImageByCode(code);
    }

    public StockResponseDTO addProduct(
            String description, int categoryId, String status,
            double price, int quantity, int clientId,
            Optional<byte[]> imageOpt) throws ValidationError {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new ValidationError(String.format("Category %d not found", categoryId));
        }
        Stock newStock =
                Stock.builder()
                        .description(description)
                        .categoryId(categoryId)
                        .status(status)
                        .price(price)
                        .quantity(quantity)
                        .createdBy(clientId)
                        .updatedBy(clientId)
                        .build();
        imageOpt.ifPresent(newStock::setImage);
        Stock savedStock = stockRepository.save(newStock);
        return StockResponseDTO.from(savedStock, optionalCategory.get());
    }

    @Transactional
    public Optional<StockResponseDTO> updateProduct(
            int code, String description, int categoryId, String status,
            double price, int quantity, int clientId, Optional<byte[]> imageOpt) throws ValidationError {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new ValidationError(String.format("Category %d not found", categoryId));
        }
        return
                stockRepository.findById(code).map(stock -> {
                    stock.setDescription(description);
                    stock.setCategoryId(categoryId);
                    stock.setStatus(status);
                    stock.setPrice(price);
                    stock.setQuantity(quantity);
                    stock.setUpdatedBy(clientId);
                    imageOpt.ifPresent(stock::setImage);
                    stockRepository.save(stock);
                    return StockResponseDTO.from(stock, optionalCategory.get());
                });
    }

    public Optional<StockResponseDTO> findStockByCode(int code) {
        return stockRepository.findStockResponseDTOById(code);
    }

    public boolean deleteProduct(int code) {
        try {
            stockRepository.deleteById(code);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

}
