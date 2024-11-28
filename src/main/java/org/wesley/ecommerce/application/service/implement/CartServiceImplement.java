package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartRepository;
import org.wesley.ecommerce.application.service.CartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public Cart update(Long cartId, Cart cart) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isPresent()) {
            Cart cartExisting = cartOptional.get();

            if (cart.getTotalPrice() != null) {
                cartExisting.setTotalPrice(cart.getTotalPrice());
            }
            if (cart.getId() != null) {
                cartExisting.setId(cart.getId());
            }
            if (cart.getItems() != null) {
                cartExisting.setItems(cart.getItems());
            }
            if (cart.getUsers() != null) {
                cartExisting.setUsers(cart.getUsers());
            }
            return cartRepository.save(cartExisting);
        } else {
            throw new NoSuchElementException("Cart not found for update");
        }
    }

    @Override
    public Cart findCartByUserId(UUID userId) {
        return cartRepository.findCartByUserId(userId);
    }
}
