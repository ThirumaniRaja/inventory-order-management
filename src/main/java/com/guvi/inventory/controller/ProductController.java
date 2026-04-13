package com.guvi.inventory.controller;

import com.guvi.inventory.DTO.ProductRequest;
import com.guvi.inventory.DTO.ProductResponse;
import com.guvi.inventory.model.OrderStatus;
import com.guvi.inventory.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products", description = "Product management and inventory endpoints (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get all active products", description = "Retrieve all active products with pagination")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponse> products = productService.getAllActiveProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Create new product", description = "Create a new product with name, price, stock, and category")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        try {
            ProductResponse response = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Update product", description = "Update existing product details")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        try {
            ProductResponse response = productService.updateProduct(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Deactivate product", description = "Deactivate a product (set status to INACTIVE)")
    @ApiResponse(responseCode = "204", description = "Product deactivated successfully")
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        try {
            productService.deactivateProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Activate product", description = "Activate a product (set status to ACTIVE)")
    @ApiResponse(responseCode = "204", description = "Product activated successfully")
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateProduct(@PathVariable Long id) {
        try {
            productService.activateProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get products by category", description = "Retrieve all products in a specific category")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Search products by name", description = "Search products using case-insensitive name matching")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @Parameter(description = "Search term") @RequestParam String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponse> products = productService.searchProductsByName(name, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Sort products by price (ascending)", description = "Get products sorted by price from lowest to highest")
    @ApiResponse(responseCode = "200", description = "Products sorted and retrieved successfully")
    @GetMapping("/sort/price-asc")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getProductsSortedByPriceAsc(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsSortedByPriceAsc(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Sort products by price (descending)", description = "Get products sorted by price from highest to lowest")
    @ApiResponse(responseCode = "200", description = "Products sorted and retrieved successfully")
    @GetMapping("/sort/price-desc")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponse>> getProductsSortedByPriceDesc(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProductResponse> products = productService.getProductsSortedByPriceDesc(pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Get low stock products", description = "Retrieve products with stock below specified threshold")
    @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "10") Long threshold) {
        List<ProductResponse> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
}

