package com.guvi.inventory.services;

import com.guvi.inventory.DTO.ProductRequest;
import com.guvi.inventory.DTO.ProductResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.model.Product;
import com.guvi.inventory.model.ProductStatus;
import com.guvi.inventory.repository.CategoryRepository;
import com.guvi.inventory.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return mapToResponse(updatedProduct);
    }

    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        return mapToResponse(product);
    }

    public Page<ProductResponse> getAllActiveProducts(Pageable pageable) {
        return productRepository.findByStatus(ProductStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndStatus(categoryId, ProductStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndStatus(name, ProductStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsSortedByPriceAsc(Pageable pageable) {
        return productRepository.findByStatusOrderByPriceAsc(ProductStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsSortedByPriceDesc(Pageable pageable) {
        return productRepository.findByStatusOrderByPriceDesc(ProductStatus.ACTIVE, pageable)
                .map(this::mapToResponse);
    }

    public List<ProductResponse> getLowStockProducts(Long threshold) {
        return productRepository.findLowStockProducts(threshold)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getStatus()
        );
    }
}

