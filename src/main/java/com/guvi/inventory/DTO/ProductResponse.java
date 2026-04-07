package com.guvi.inventory.DTO;

import com.guvi.inventory.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long stockQuantity;
    private Long categoryId;
    private String categoryName;
    private ProductStatus status;
}

