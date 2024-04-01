package com.demo.sqlite.services;

import com.demo.sqlite.dtos.StockResponseDTO;
import com.demo.sqlite.exceptions.ValidationError;
import com.demo.sqlite.models.Category;
import com.demo.sqlite.models.Stock;
import com.demo.sqlite.repositories.CategoryRepository;
import com.demo.sqlite.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final CategoryRepository categoryRepository;

    public StockService(@Autowired StockRepository stockRepository,
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

    public Stock addProduct(String description, int categoryId, String status,
                            double price, int quantity, int clientId,
                            Optional<byte[]> imageOpt) throws ValidationError {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new ValidationError();
        }
        Stock newStock =
                Stock.builder()
                        .description(description)
                        .category_id(categoryId)
                        .status(status)
                        .price(price)
                        .quantity(quantity)
                        .updated_by(clientId)
                        .created_by(clientId)
                        .build();
        imageOpt.ifPresent(newStock::setImage);
        return stockRepository.save(newStock);
    }

    @Transactional
    public Optional<StockResponseDTO> updateProduct(int code, String description, int categoryId, String status,
                                                    double price, int quantity, int clientId, Optional<byte[]> imageOpt) throws ValidationError {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Stock> stockOptional = stockRepository.findById(code);
        if (optionalCategory.isEmpty()) {
            throw new ValidationError();
        }
        return
                stockOptional.map(stock -> {
                    stock.setDescription(description);
                    stock.setCategory_id(categoryId);
                    stock.setStatus(status);
                    stock.setPrice(price);
                    stock.setQuantity(quantity);
                    stock.setUpdated_by(clientId);
                    imageOpt.ifPresent(stock::setImage);
                    stockRepository.save(stock);
                    return StockResponseDTO.from(stock, optionalCategory.get());
                });
    }

    public Optional<StockResponseDTO> findStockByCode(int code) {
        return stockRepository.findStockResponseDTOById(code);
    }

    public boolean deleteProduct(int code) {
        stockRepository.deleteById(code);
        try {
            stockRepository.deleteById(code);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
    }

}
