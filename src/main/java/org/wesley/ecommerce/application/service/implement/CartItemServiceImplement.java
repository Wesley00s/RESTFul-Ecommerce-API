package org.wesley.ecommerce.application.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.service.CartItemService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;

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
}
