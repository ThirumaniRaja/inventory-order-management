package com.guvi.inventory.controller;

import com.guvi.inventory.DTO.CreateOrderRequest;
import com.guvi.inventory.DTO.OrderResponse;
import com.guvi.inventory.model.OrderStatus;
import com.guvi.inventory.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Order management endpoints (User only)")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get user's orders", description = "Retrieve all orders for the authenticated user with pagination")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        Page<OrderResponse> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Place new order", description = "Create and place a new order with multiple items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order placed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            OrderResponse response = orderService.createOrder(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Get order details", description = "Retrieve details of a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - order belongs to another user")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> getOrderDetails(
            Authentication authentication,
            @PathVariable Long orderId) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            OrderResponse response = orderService.getOrderById(orderId, userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Cancel order", description = "Cancel an order and restore stock to inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - order belongs to another user")
    })
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            orderService.cancelOrder(orderId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get orders by status", description = "Retrieve user's orders filtered by status (CREATED/CONFIRMED/CANCELLED)")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            Authentication authentication,
            @PathVariable OrderStatus status,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        Page<OrderResponse> orders = orderService.getUserOrdersByStatus(userId, status, pageable);
        return ResponseEntity.ok(orders);
    }
}

