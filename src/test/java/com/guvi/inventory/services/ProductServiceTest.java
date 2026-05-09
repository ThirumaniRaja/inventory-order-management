package com.guvi.inventory.services;

import com.guvi.inventory.DTO.ProductRequest;
import com.guvi.inventory.DTO.ProductResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.model.Product;
import com.guvi.inventory.model.ProductStatus;
import com.guvi.inventory.repository.CategoryRepository;
import com.guvi.inventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository productRepository;
    @Mock CategoryRepository categoryRepository;
    @InjectMocks ProductService productService;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setActive(true);

        product = new Product();
        product.setId(10L);
        product.setName("Keyboard");
        product.setDescription("Mechanical");
        product.setPrice(new BigDecimal("1500"));
        product.setStockQuantity(20L);
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);
    }

    @Test
    void createProduct_shouldCreateAndReturn() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenReturn(product);
        ProductResponse r = productService.createProduct(
                new ProductRequest("Keyboard", "Mechanical", new BigDecimal("1500"), 20L, 1L));
        assertEquals(10L, r.getId());
        assertEquals(ProductStatus.ACTIVE, r.getStatus());
        verify(productRepository).save(any());
    }

    @Test
    void createProduct_shouldThrowWhenCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> productService.createProduct(new ProductRequest("K", "d", BigDecimal.TEN, 1L, 99L)));
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_shouldUpdateFields() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        ProductResponse r = productService.updateProduct(10L,
                new ProductRequest("Mouse", "Wireless", new BigDecimal("800"), 15L, 1L));
        assertEquals("Mouse", r.getName());
        assertEquals(new BigDecimal("800"), r.getPrice());
    }

    @Test
    void updateProduct_shouldThrowWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> productService.updateProduct(99L,
                        new ProductRequest("M", "d", BigDecimal.ONE, 1L, 1L)));
    }

    @Test
    void deactivateProduct_shouldSetStatusInactive() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        productService.deactivateProduct(10L);
        verify(productRepository).save(argThat(p -> p.getStatus() == ProductStatus.INACTIVE));
    }

    @Test
    void activateProduct_shouldSetStatusActive() {
        product.setStatus(ProductStatus.INACTIVE);
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        productService.activateProduct(10L);
        verify(productRepository).save(argThat(p -> p.getStatus() == ProductStatus.ACTIVE));
    }

    @Test
    void deactivateProduct_shouldThrowWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.deactivateProduct(99L));
    }

    @Test
    void getProductById_shouldReturn() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        ProductResponse r = productService.getProductById(10L);
        assertEquals(10L, r.getId());
        assertEquals("Electronics", r.getCategoryName());
    }

    @Test
    void getProductById_shouldThrowWhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(99L));
    }

    @Test
    void getLowStockProducts_shouldReturnList() {
        when(productRepository.findLowStockProducts(5L)).thenReturn(List.of(product));
        List<ProductResponse> result = productService.getLowStockProducts(5L);
        assertEquals(1, result.size());
        assertEquals("Keyboard", result.get(0).getName());
    }
}
