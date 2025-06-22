package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.List;
import java.util.UUID;

@Service
public interface CartService {
    void addProductToCart(Cart cart, Long productId, Integer quantity);

    void removeProductFromCart(Cart cart, Long productId, int quantity);

    void toggleItemSelection(Cart cart, Long itemId, boolean selected);

    Cart create(Cart cart);

    Cart findById(Long id);

    List<Cart> findAll();

    Cart update(Long cartId, Cart cart);

    Cart findCartByUserId(UUID userId);
}
