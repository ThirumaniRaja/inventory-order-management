package com.guvi.inventory.controller;

import com.guvi.inventory.DTO.CreateOrderRequest;
import com.guvi.inventory.DTO.OrderResponse;
import com.guvi.inventory.model.OrderStatus;
import com.guvi.inventory.services.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders for the current user (requires USER role)
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        Page<OrderResponse> orders = orderService.getUserOrders(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Place a new order (requires USER role)
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(
            Authentication authentication,
            @RequestBody CreateOrderRequest request) {
        try {
            Long userId = (Long) authentication.getPrincipal();
            OrderResponse response = orderService.createOrder(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Get order details (requires USER role)
     */
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

    /**
     * Cancel an order (requires USER role)
     */
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

    /**
     * Get order history for specific status (requires USER role)
     */
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

