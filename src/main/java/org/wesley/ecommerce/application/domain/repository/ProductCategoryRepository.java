package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wesley.ecommerce.application.domain.model.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Page<ProductCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<ProductCategory> findByName(String name);
}
