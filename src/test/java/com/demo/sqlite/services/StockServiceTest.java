package com.demo.sqlite.services;

import com.demo.sqlite.dtos.StockResponseDTO;
import com.demo.sqlite.exceptions.ValidationError;
import com.demo.sqlite.models.Category;
import com.demo.sqlite.models.Stock;
import com.demo.sqlite.repositories.CategoryRepository;
import com.demo.sqlite.repositories.StockRepository;
import lombok.AllArgsConstructor;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StockService stockService;

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

    @AllArgsConstructor
    public static class StockMatcher implements ArgumentMatcher<Stock> {

        private Stock expectedStock;

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

    @Test
    void testAddProduct() throws ValidationError {
        int clientId = 15;
        // Mocking data
        when(categoryRepository.findById(eq(stock1.getCategoryId()))).thenReturn(Optional.of(category1));
        when(stockRepository.save(argThat(new StockMatcher(stock1)))).thenReturn(stock1);

        // Test
        Stock result = stockService.addProduct(
                stock1.getDescription(),
                stock1.getCategoryId(),
                stock1.getStatus(),
                stock1.getPrice(),
                stock1.getQuantity(),
                clientId,
                Optional.ofNullable(stock1.getImage()));

        // Assertion
        assertEquals(stock1, result);
        verify(categoryRepository, times(1)).findById(stock1.getCategoryId());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

//
//    @Test
//    void testUpdateProduct() throws ValidationError {
//        // Mocking data
//        Category category = new Category();
//        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
//        Stock stock = new Stock();
//        when(stockRepository.findById(anyInt())).thenReturn(Optional.of(stock));
//        StockResponseDTO expectedResponse = new StockResponseDTO();
//        when(stockRepository.save(any(Stock.class))).thenReturn(stock);
//
//        // Test
//        Optional<StockResponseDTO> result = stockService.updateProduct(123, "new description", 1, "new status", 20.0, 10, 1, Optional.empty());
//
//        // Assertion
//        assertTrue(result.isPresent());
//        assertEquals(expectedResponse, result.get());
//        verify(categoryRepository, times(1)).findById(anyInt());
//        verify(stockRepository, times(1)).findById(anyInt());
//        verify(stockRepository, times(1)).save(any(Stock.class));
//    }
//
//    @Test
//    void testFindStockByCode() {
//        // Mocking data
//        StockResponseDTO expectedResponse = new StockResponseDTO();
//        when(stockRepository.findStockResponseDTOById(anyInt())).thenReturn(Optional.of(expectedResponse));
//
//        // Test
//        Optional<StockResponseDTO> result = stockService.findStockByCode(123);
//
//        // Assertion
//        assertTrue(result.isPresent());
//        assertEquals(expectedResponse, result.get());
//        verify(stockRepository, times(1)).findStockResponseDTOById(anyInt());
//    }
//
//    @Test
//    void testDeleteProduct() {
//        // Mocking data
//        doNothing().when(stockRepository).deleteById(anyInt());
//
//        // Test
//        boolean result = stockService.deleteProduct(123);
//
//        // Assertion
//        assertTrue(result);
//        verify(stockRepository, times(1)).deleteById(anyInt());
//    }
}