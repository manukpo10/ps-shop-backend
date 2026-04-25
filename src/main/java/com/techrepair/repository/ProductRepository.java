package com.techrepair.repository;

import com.techrepair.model.Product;
import com.techrepair.model.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    Optional<Product> findBySku(String sku);
    List<Product> findByActive(boolean active);
}