package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.* FROM product AS p JOIN cart_item AS ci ON p.id = ci.product_id WHERE ci.cart_id = ?1", nativeQuery = true)
    List<Product> findProductsByCart(Long cartId);
}
