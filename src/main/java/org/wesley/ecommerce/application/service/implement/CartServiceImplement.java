package org.wesley.ecommerce.application.service.implement;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.service.CartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class CartServiceImplement implements CartService {
    private final CartItemRepository cartItemRepository;

    public CartServiceImplement(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart create(Cart cart) {
        return cartItemRepository.save(cart);
    }

    @Override
    public Cart findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Cart> findAll() {
        return cartItemRepository.findAll();
    }

    @Override
    public Cart findCartByUserId(UUID userId, Long cartId) {
        return cartItemRepository.findCartByUserId(userId, cartId);
    }

    @Override
    @Transactional
    public void disableCart(Long cartId) {
        cartItemRepository.disableCart(cartId);
    }
}
