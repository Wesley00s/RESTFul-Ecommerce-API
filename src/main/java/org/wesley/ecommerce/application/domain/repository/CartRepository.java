package org.wesley.ecommerce.application.domain.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM cart AS c WHERE c.user_id = ?1 AND c.id = ?2",
            nativeQuery = true)
    Cart findCartByUserId(UUID userId, Long cartId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart SET is_active = false WHERE id = ?1", nativeQuery = true)
    void disableCart(Long cartId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO cart_products (cart_id, products_id) VALUES (?1, ?2)", nativeQuery = true)
    void addToCart(Long cartId, Long productId);
}
