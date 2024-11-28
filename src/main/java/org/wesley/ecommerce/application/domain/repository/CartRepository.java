package org.wesley.ecommerce.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM cart WHERE user_id = ?1",
            nativeQuery = true)
    Cart findCartByUserId(UUID userId);
}