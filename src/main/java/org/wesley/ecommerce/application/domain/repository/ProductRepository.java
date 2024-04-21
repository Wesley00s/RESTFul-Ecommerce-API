package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wesley.ecommerce.application.domain.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
