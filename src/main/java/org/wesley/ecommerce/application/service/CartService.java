package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {
    Cart create(Cart cart);

    Cart findById(Long id);

    List<Cart> findAll();

    Cart findCartByUserId(UUID userId, Long cartId);

    void disableCart(Long cartId);
}
