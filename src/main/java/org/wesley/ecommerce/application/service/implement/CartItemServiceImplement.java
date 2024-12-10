package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.service.CartItemService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemServiceImplement(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartItem findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public CartItem update(Long cartItemId, CartItem cartItem) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItemExisting = cartItemOptional.get();

            if (cartItem.getPrice() != null) {
                cartItemExisting.setPrice(cartItem.getPrice());
            }
            if (cartItem.getId() != null) {
                cartItemExisting.setId(cartItem.getId());
            }
            if (cartItem.getQuantity() != null) {
                cartItemExisting.setQuantity(cartItem.getQuantity());
            }
            if (cartItem.getCart() != null) {
                cartItemExisting.setCart(cartItem.getCart());
            }
            if (cartItem.getCart() != null) {
                cartItemExisting.setProduct(cartItem.getProduct());
            }
            return cartItemRepository.save(cartItemExisting);
        } else {
            throw new NoSuchElementException("Cart not found for update");
        }
    }

    @Override
    public void removeAllFromCartItem(Long productId, Long cartId) {
        cartItemRepository.removeAllFromCart(productId, cartId);
    }

    @Override
    public void removeOnlyFromCartItem(Long productId, Long cartId, int quantity) {
        cartItemRepository.removeOnlyFromCartItem(productId, cartId, quantity);
    }

    @Override
    public void addItemToCart(Long cartId, Long productId, Integer quantity, Double price) {
        cartItemRepository.addItemToCart(cartId, productId, quantity, price);
    }

    @Override
    public void updateTotalPrice(Double newTotalPrice, Long productId, Long cartId) {
        if (newTotalPrice == null || newTotalPrice <= 0) {
            throw new IllegalArgumentException("Total price must be greater than zero.");
        }
        cartItemRepository.updateTotalPrice(newTotalPrice, productId, cartId);
    }
}
