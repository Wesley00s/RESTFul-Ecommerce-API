package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.CartItem;

@Service
public interface CartItemService {

    CartItem update(Long cartItemId, CartItem cartItem);

    void removeAllFromCartItem(Long productId, Long cartId);

    void removeOnlyFromCartItem(Long productId, Long cartId, int quantity);

    void addToCartItem(Long cartId, Long productId, Integer quantity, Double price);

    void updateTotalPrice(Double newTotalPrice, Long productId, Long cartId);
}
