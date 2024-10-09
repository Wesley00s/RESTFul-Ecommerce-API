package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.User;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM cart AS c WHERE c.user_id = ?1 AND c.cart_id = ?2",
    nativeQuery = true)
    Cart findCartByUserId(UUID userId, Long cartId);
}
