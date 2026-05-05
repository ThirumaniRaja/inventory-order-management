package com.guvi.inventory.DTO;

import com.guvi.inventory.model.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    void registerRequest_shouldFailForInvalidEmail() {
        RegisterRequest request = new RegisterRequest("john", "secret", "invalid-email", Role.USER);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "email".equals(v.getPropertyPath().toString())));
    }

    @Test
    void createOrderRequest_shouldFailForEmptyItems() {
        CreateOrderRequest request = new CreateOrderRequest(List.of());

        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "items".equals(v.getPropertyPath().toString())));
    }

    @Test
    void orderItemRequest_shouldFailForNonPositiveQuantity() {
        OrderItemRequest request = new OrderItemRequest(1L, 0L);

        Set<ConstraintViolation<OrderItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> "quantity".equals(v.getPropertyPath().toString())));
    }
}

