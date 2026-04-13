package com.guvi.inventory.controller;

import com.guvi.inventory.DTO.ProductResponse;
import com.guvi.inventory.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Inventory", description = "Inventory management and stock monitoring endpoints (Admin only)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final ProductService productService;

    public InventoryController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Get product stock", description = "Retrieve stock information for a specific product")
    @ApiResponse(responseCode = "200", description = "Stock information retrieved successfully")
    @GetMapping("/{productId}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> getProductStock(@PathVariable Long productId) {
        try {
            ProductResponse product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get low stock products", description = "Retrieve all products with stock below specified threshold")
    @ApiResponse(responseCode = "200", description = "Low stock products retrieved successfully")
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(
            @Parameter(description = "Stock threshold") @RequestParam(defaultValue = "10") Long threshold) {
        List<ProductResponse> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
}

