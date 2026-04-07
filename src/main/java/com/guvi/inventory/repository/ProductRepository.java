package com.guvi.inventory.repository;

import com.guvi.inventory.model.Product;
import com.guvi.inventory.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Product> findByNameContainingIgnoreCaseAndStatus(String name, ProductStatus status, Pageable pageable);
    
    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' AND p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Long threshold);
    
    Page<Product> findByStatusOrderByPriceAsc(ProductStatus status, Pageable pageable);
    
    Page<Product> findByStatusOrderByPriceDesc(ProductStatus status, Pageable pageable);
}

