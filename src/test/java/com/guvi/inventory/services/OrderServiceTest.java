package com.guvi.inventory.services;

import com.guvi.inventory.DTO.CreateOrderRequest;
import com.guvi.inventory.DTO.OrderItemRequest;
import com.guvi.inventory.DTO.OrderResponse;
import com.guvi.inventory.model.Order;
import com.guvi.inventory.model.OrderItem;
import com.guvi.inventory.model.OrderStatus;
import com.guvi.inventory.model.Product;
import com.guvi.inventory.model.ProductStatus;
import com.guvi.inventory.model.Role;
import com.guvi.inventory.model.User;
import com.guvi.inventory.repository.OrderRepository;
import com.guvi.inventory.repository.ProductRepository;
import com.guvi.inventory.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock OrderRepository orderRepository;
    @Mock ProductRepository productRepository;
    @Mock UserRepository userRepository;
    @InjectMocks OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setRole(Role.USER);
        user.setActive(true);

        product = new Product();
        product.setId(10L);
        product.setName("Keyboard");
        product.setPrice(new BigDecimal("500"));
        product.setStockQuantity(10L);
        product.setStatus(ProductStatus.ACTIVE);
    }

    @Test
    void createOrder_shouldCreateOrderAndReduceStock() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(100L);
            return o;
        });
        OrderResponse r = orderService.createOrder(1L,
                new CreateOrderRequest(List.of(new OrderItemRequest(10L, 2L))));
        assertEquals(100L, r.getId());
        assertEquals(OrderStatus.CONFIRMED, r.getStatus());
        assertEquals(new BigDecimal("1000"), r.getTotalAmount());
        assertEquals(8L, product.getStockQuantity());
    }

    @Test
    void createOrder_shouldFailWhenItemsAreEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L, new CreateOrderRequest(List.of())));
        verifyNoInteractions(userRepository);
    }

    @Test
    void createOrder_shouldFailWhenQuantityIsZero() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L,
                        new CreateOrderRequest(List.of(new OrderItemRequest(10L, 0L)))));
    }

    @Test
    void createOrder_shouldFailWhenStockInsufficient() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L,
                        new CreateOrderRequest(List.of(new OrderItemRequest(10L, 99L)))));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_shouldFailForInactiveProduct() {
        product.setStatus(ProductStatus.INACTIVE);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L,
                        new CreateOrderRequest(List.of(new OrderItemRequest(10L, 1L)))));
    }

    private Order buildConfirmedOrder() {
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(2L);
        item.setUnitPrice(new BigDecimal("500"));
        item.setSubtotal(new BigDecimal("1000"));
        Order order = new Order();
        order.setId(100L);
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(new BigDecimal("1000"));
        order.addItem(item);
        return order;
    }

    @Test
    void cancelOrder_shouldCancelAndRestoreStock() {
        Order order = buildConfirmedOrder();
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        orderService.cancelOrder(100L, 1L);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(12L, product.getStockQuantity());
    }

    @Test
    void cancelOrder_shouldThrowWhenAlreadyCancelled() {
        Order order = buildConfirmedOrder();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(100L, 1L));
    }

    @Test
    void cancelOrder_shouldThrowForWrongUser() {
        Order order = buildConfirmedOrder();
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(100L, 999L));
    }

    @Test
    void getOrderById_shouldReturnForCorrectUser() {
        Order order = buildConfirmedOrder();
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        OrderResponse r = orderService.getOrderById(100L, 1L);
        assertEquals(100L, r.getId());
        assertEquals(1, r.getItems().size());
    }

    @Test
    void getOrderById_shouldThrowForWrongUser() {
        Order order = buildConfirmedOrder();
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(100L, 999L));
    }

    @Test
    void getOrderById_shouldThrowWhenNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(999L, 1L));
    }
}
