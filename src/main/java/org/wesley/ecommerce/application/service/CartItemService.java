package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;

import java.util.List;

@Service
public interface CartItemService {
    Cart create(Cart cart);
    Cart findById(Long id);
    List<Cart> findAll();
    Cart getProducts(Long id, Long productId, Integer quantity);
    void delete(Cart cart);
}
