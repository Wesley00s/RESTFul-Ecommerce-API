package org.wesley.ecommerce.application.domain.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM cart AS c WHERE c.user_id = ?1 AND c.cart_id = ?2",
            nativeQuery = true)
    Cart findCartByUserId(UUID userId, Long cartId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart SET is_active = false WHERE cart_id = ?1", nativeQuery = true)
    void disableCart(Long cartId);

}
