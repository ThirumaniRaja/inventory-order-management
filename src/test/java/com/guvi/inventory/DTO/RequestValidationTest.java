package com.guvi.inventory.DTO;

import com.guvi.inventory.model.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void teardown() {
        factory.close();
    }

    @Test
    void register_blankUsername_fails() {
        Set<ConstraintViolation<RegisterRequest>> v =
                validator.validate(new RegisterRequest("", "pass", "a@b.com", Role.USER));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "username".equals(x.getPropertyPath().toString())));
    }

    @Test
    void register_invalidEmail_fails() {
        Set<ConstraintViolation<RegisterRequest>> v =
                validator.validate(new RegisterRequest("john", "pass", "not-email", Role.USER));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "email".equals(x.getPropertyPath().toString())));
    }

    @Test
    void register_nullRole_fails() {
        Set<ConstraintViolation<RegisterRequest>> v =
                validator.validate(new RegisterRequest("john", "pass", "a@b.com", null));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "role".equals(x.getPropertyPath().toString())));
    }

    @Test
    void register_validData_passes() {
        assertTrue(validator.validate(
                new RegisterRequest("john", "pass", "john@test.com", Role.USER)).isEmpty());
    }

    @Test
    void login_blankUsername_fails() {
        assertFalse(validator.validate(new LoginRequest("", "pass")).isEmpty());
    }

    @Test
    void login_blankPassword_fails() {
        assertFalse(validator.validate(new LoginRequest("john", "")).isEmpty());
    }

    @Test
    void category_blankName_fails() {
        assertFalse(validator.validate(new CategoryRequest("", "desc")).isEmpty());
    }

    @Test
    void product_nullPrice_fails() {
        Set<ConstraintViolation<ProductRequest>> v =
                validator.validate(new ProductRequest("KB", "d", null, 10L, 1L));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "price".equals(x.getPropertyPath().toString())));
    }

    @Test
    void product_zeroPrice_fails() {
        assertFalse(validator.validate(
                new ProductRequest("KB", "d", BigDecimal.ZERO, 10L, 1L)).isEmpty());
    }

    @Test
    void product_negativeStock_fails() {
        Set<ConstraintViolation<ProductRequest>> v =
                validator.validate(new ProductRequest("KB", "d", BigDecimal.TEN, -1L, 1L));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "stockQuantity".equals(x.getPropertyPath().toString())));
    }

    @Test
    void orderItem_zeroQty_fails() {
        Set<ConstraintViolation<OrderItemRequest>> v =
                validator.validate(new OrderItemRequest(1L, 0L));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "quantity".equals(x.getPropertyPath().toString())));
    }

    @Test
    void orderItem_nullProductId_fails() {
        Set<ConstraintViolation<OrderItemRequest>> v =
                validator.validate(new OrderItemRequest(null, 1L));
        assertFalse(v.isEmpty());
        assertTrue(v.stream().anyMatch(x -> "productId".equals(x.getPropertyPath().toString())));
    }

    @Test
    void createOrder_emptyItems_fails() {
        assertFalse(validator.validate(new CreateOrderRequest(List.of())).isEmpty());
    }

    @Test
    void createOrder_nullItems_fails() {
        assertFalse(validator.validate(new CreateOrderRequest(null)).isEmpty());
    }
}
