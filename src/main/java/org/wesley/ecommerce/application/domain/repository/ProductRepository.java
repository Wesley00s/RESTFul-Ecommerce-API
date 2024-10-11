package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "UPDATE product SET stock_quantity = ?2 WHERE id = ?1",
            nativeQuery = true)
    void updateQuantityStock(Long productId, int quantity);

    @Query(value = "SELECT p.* FROM product AS p JOIN cart_products AS cp ON p.id = cp.products_id WHERE cp.cart_id = ?1", nativeQuery = true)
    List<Product> findProductsByCart(Long cartId);
}
