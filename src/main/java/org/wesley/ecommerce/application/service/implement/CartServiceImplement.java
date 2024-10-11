package org.wesley.ecommerce.application.service.implement;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartRepository;
import org.wesley.ecommerce.application.service.CartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class CartServiceImplement implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImplement(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart create(Cart cart) {

        return cartRepository.save(cart);
    }


    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public void addToCart(Long cartId, Long productId) {
        cartRepository.addToCart(cartId, productId);
    }


    @Override
    public Cart findCartByUserId(UUID userId, Long cartId) {
        return cartRepository.findCartByUserId(userId, cartId);
    }

    @Override
    @Transactional
    public void disableCart(Long cartId) {
        cartRepository.disableCart(cartId);
    }
}
