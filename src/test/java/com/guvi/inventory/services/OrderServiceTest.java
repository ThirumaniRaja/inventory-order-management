package com.guvi.inventory.services;

import com.guvi.inventory.DTO.CreateOrderRequest;
import com.guvi.inventory.DTO.OrderItemRequest;
import com.guvi.inventory.DTO.OrderResponse;
import com.guvi.inventory.model.Order;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

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
        product.setPrice(new BigDecimal("499.99"));
        product.setStockQuantity(10L);
        product.setStatus(ProductStatus.ACTIVE);
    }

    @Test
    void createOrder_shouldCreateOrderAndReduceStock() {
        CreateOrderRequest request = new CreateOrderRequest(List.of(new OrderItemRequest(10L, 2L)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        OrderResponse response = orderService.createOrder(1L, request);

        assertEquals(100L, response.getId());
        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        assertEquals(new BigDecimal("999.98"), response.getTotalAmount());
        assertEquals(1, response.getItems().size());
        assertEquals(8L, product.getStockQuantity());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createOrder_shouldFailWhenItemsAreEmpty() {
        CreateOrderRequest request = new CreateOrderRequest(List.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L, request));

        assertEquals("Order must contain at least one item", ex.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void createOrder_shouldFailWhenQuantityIsNotPositive() {
        CreateOrderRequest request = new CreateOrderRequest(List.of(new OrderItemRequest(10L, 0L)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L, request));

        assertEquals("Quantity must be greater than 0", ex.getMessage());
        verify(productRepository, times(0)).findById(any(Long.class));
    }

    @Test
    void createOrder_shouldFailWhenStockIsInsufficient() {
        CreateOrderRequest request = new CreateOrderRequest(List.of(new OrderItemRequest(10L, 11L)));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(1L, request));

        assertTrue(ex.getMessage().contains("Insufficient stock for product"));
        verify(orderRepository, times(0)).save(any(Order.class));
    }
}

