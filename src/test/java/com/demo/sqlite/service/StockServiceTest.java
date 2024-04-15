package com.demo.sqlite.service;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.response.StockResponseDTO;
import com.demo.sqlite.model.entity.Category;
import com.demo.sqlite.model.entity.Stock;
import com.demo.sqlite.repository.CategoryRepository;
import com.demo.sqlite.repository.StockRepository;
import com.demo.sqlite.service.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StockServiceImpl stockService;

    public record StockMatcher(Stock expectedStock) implements ArgumentMatcher<Stock> {

        @Override
        public boolean matches(Stock actualStock) {
            // Perform specific checks on the fields of the Stock object
            return expectedStock.getPrice() == actualStock.getPrice() &&
                    expectedStock.getStatus().equals(actualStock.getStatus()) &&
                    expectedStock.getDescription().equals(actualStock.getDescription()) &&
                    expectedStock.getQuantity() == actualStock.getQuantity() &&
                    Arrays.equals(expectedStock.getImage(), actualStock.getImage()) &&
                    expectedStock.getCategoryId() == actualStock.getCategoryId();
        }
    }

    private final String SEARCH_PHRASE = "searchPhrase";

    private final Pageable pagination = PageRequest.of(0, 10);

    private Stock stock1;
    private Stock stock2;
    private Category category1;
    private Category category2;
    private List<StockResponseDTO> stockResponseDTOList;

    @BeforeEach
    public void init() {
        stock1 = Stock.builder()
                .code(1)
                .description("description 1")
                .quantity(15)
                .categoryId(1)
                .price(25)
                .status("active")
                .image(new byte[]{10, 20})
                .createdBy(5)
                .updatedBy(6)
                .build();
        stock2 = Stock.builder()
                .code(2)
                .description("description 2")
                .quantity(100)
                .categoryId(2)
                .price(120)
                .status("active")
                .build();
        category1 = Category.builder()
                .id(1)
                .category("category 1")
                .description("description 1")
                .build();
        category2 = Category.builder()
                .id(2)
                .category("category 2")
                .description("description 2")
                .build();
        stockResponseDTOList = List.of(
                StockResponseDTO.from(stock1, category1),
                StockResponseDTO.from(stock2, category2)
        );
    }

    @Test
    void testGetAllStocksWhenNoFoundStocks() {
        // Mocking data
        when(stockRepository.filterByPhraseAndPagination(SEARCH_PHRASE, pagination)).thenReturn(Page.empty());

        // Test
        List<StockResponseDTO> result = stockService.getAllStocks(SEARCH_PHRASE, pagination);

        // Assertion
        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).filterByPhraseAndPagination(SEARCH_PHRASE, pagination);
    }

    @Test
    void testGetAllStocksWhenFoundStocks() {
        Page<StockResponseDTO> stockResponseDTOS = new PageImpl<>(stockResponseDTOList);
        // Mocking data
        when(stockRepository.filterByPhraseAndPagination(SEARCH_PHRASE, pagination)).thenReturn(stockResponseDTOS);

        // Test
        List<StockResponseDTO> result = stockService.getAllStocks(SEARCH_PHRASE, pagination);

        // Assertion
        assertThat(result.size()).isEqualTo(2);
        assertEquals(result, stockResponseDTOList);
        verify(stockRepository, times(1)).filterByPhraseAndPagination(SEARCH_PHRASE, pagination);
    }

    @Test
    void testFindImageByCodeWhenFoundImage() {
        // Mocking data
        int imageCode = 123;
        byte[] expectedImage = {10, 20};
        when(stockRepository.findImageByCode(imageCode)).thenReturn(Optional.of(expectedImage));

        // Test
        Optional<byte[]> result = stockService.findImageByCode(imageCode);

        // Assertion
        assertTrue(result.isPresent());
        assertEquals(expectedImage, result.get());
        verify(stockRepository, times(1)).findImageByCode(imageCode);
    }

    @Test
    void testFindImageByCodeWhenNoFoundImage() {
        // Mocking data
        int imageCode = 123;
        when(stockRepository.findImageByCode(imageCode)).thenReturn(Optional.empty());

        // Test
        Optional<byte[]> result = stockService.findImageByCode(imageCode);

        // Assertion
        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findImageByCode(imageCode);
    }


    @Test
    void testAddProduct() throws ValidationError {
        int clientId = 15;
        // Mocking data
        when(categoryRepository.findById(stock1.getCategoryId())).thenReturn(Optional.of(category1));
        when(stockRepository.save(argThat(new StockMatcher(stock1)))).thenReturn(stock1);

        // Test
        StockResponseDTO result = stockService.addProduct(
                stock1.getDescription(),
                stock1.getCategoryId(),
                stock1.getStatus(),
                stock1.getPrice(),
                stock1.getQuantity(),
                clientId,
                Optional.ofNullable(stock1.getImage()));

        // Assertion
        assertEquals(stock1.getCategoryId(), result.getCategory().getId());
        assertEquals(stock1.getQuantity(), result.getQuantity());
        assertEquals(stock1.getPrice(), result.getPrice());
        assertEquals(stock1.getStatus(), result.getStatus());
        assertEquals(stock1.getDescription(), result.getDescription());
        assertEquals(stock1.getCode(), result.getCode());
        verify(categoryRepository, times(1)).findById(stock1.getCategoryId());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void testAddProductWhenCategoryNotFound() throws ValidationError {
        int clientId = 15;
        // Mocking data
        when(categoryRepository.findById(stock1.getCategoryId())).thenReturn(Optional.empty());

        // Test
        assertThrows(ValidationError.class, () -> {
            stockService.addProduct(
                    stock1.getDescription(),
                    stock1.getCategoryId(),
                    stock1.getStatus(),
                    stock1.getPrice(),
                    stock1.getQuantity(),
                    clientId,
                    Optional.ofNullable(stock1.getImage())
            );
        });
        // Assertion
        verify(categoryRepository, times(1)).findById(stock1.getCategoryId());
    }


    @Test
    void testUpdateProduct() throws ValidationError {
        int clientId = 15;
        // Mocking data
        when(categoryRepository.findById(stock1.getCategoryId())).thenReturn(Optional.of(category1));
        when(stockRepository.findById(stock1.getCode())).thenReturn(Optional.of(stock1));
        when(stockRepository.save(argThat(new StockMatcher(stock1)))).thenReturn(stock1);

        // Test
        Optional<StockResponseDTO> result =
                stockService.updateProduct(
                        stock1.getCode(),
                        stock1.getDescription(),
                        stock1.getCategoryId(),
                        stock1.getStatus(),
                        stock1.getPrice(),
                        stock1.getQuantity(),
                        clientId,
                        Optional.ofNullable(stock1.getImage())
                );

        // Assertion
        assertTrue(result.isPresent());
        assertEquals(stock1.getCategoryId(), result.get().getCategory().getId());
        assertEquals(stock1.getQuantity(), result.get().getQuantity());
        assertEquals(stock1.getPrice(), result.get().getPrice());
        assertEquals(stock1.getStatus(), result.get().getStatus());
        assertEquals(stock1.getDescription(), result.get().getDescription());
        assertEquals(stock1.getCode(), result.get().getCode());

        verify(categoryRepository, times(1)).findById(stock1.getCategoryId());
        verify(stockRepository, times(1)).findById(stock1.getCode());
        verify(stockRepository, times(1)).save(any(Stock.class));

    }

    @Test
    void testUpdateProductWhenCategoryNotFound() throws ValidationError {
        int clientId = 15;
        // Mocking data
        when(categoryRepository.findById(stock1.getCategoryId())).thenReturn(Optional.empty());

        // Test
        assertThrows(ValidationError.class, () -> {
            stockService.updateProduct(
                    stock1.getCode(),
                    stock1.getDescription(),
                    stock1.getCategoryId(),
                    stock1.getStatus(),
                    stock1.getPrice(),
                    stock1.getQuantity(),
                    clientId,
                    Optional.ofNullable(stock1.getImage())
            );
        });

        // Assertion
        verify(categoryRepository, times(1)).findById(stock1.getCategoryId());
    }

    @Test
    void testFindStockByCode() {
        StockResponseDTO responseDTO = StockResponseDTO.from(stock1, category1);
        // Mocking data
        when(stockRepository.findStockResponseDTOById(stock1.getCode()))
                .thenReturn(Optional.of(responseDTO));

        // Test
        Optional<StockResponseDTO> result = stockService.findStockByCode(stock1.getCode());

        // Assertion
        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
        verify(stockRepository, times(1)).findStockResponseDTOById(anyInt());
    }

    @Test
    void testFindStockByCodeWhenStockNotFound() {
        // Mocking data
        when(stockRepository.findStockResponseDTOById(stock1.getCode()))
                .thenReturn(Optional.empty());
        // Test
        Optional<StockResponseDTO> result = stockService.findStockByCode(stock1.getCode());

        // Assertion
        assertTrue(result.isEmpty());
        verify(stockRepository, times(1)).findStockResponseDTOById(anyInt());
    }


    @Test
    void testDeleteProduct() {
        // Mocking data
        doNothing().when(stockRepository).deleteById(stock1.getCode());

        // Test
        boolean result = stockService.deleteProduct(stock1.getCode());

        // Assertion
        assertTrue(result);
        verify(stockRepository, times(1)).deleteById(stock1.getCode());
    }
}