package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.service.CartService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * This class implements the CartItemService interface and provides methods for managing cart items.
 * It uses Spring's @Service annotation to mark it as a service component.
 */
@Service
public class CartServiceImplement implements CartService {
    private final CartItemRepository cartItemRepository;

    /**
     * Constructor for CartItemServiceImplement.
     *
     * @param cartItemRepository The repository for managing cart items.
     * @param productRepository  The repository for managing products.
     */
    public CartServiceImplement(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

//    @Override
//    public void linkUsersToCart(UUID userId, Long cartId) {
//        this.cartItemRepository.linkUserToCart(cartId, userId);
//    }

    /**
     * Creates a new cart item.
     *
     * @param cart The cart item to be created.
     * @return The created cart item.
     */
    @Override
    public Cart create(Cart cart) {
        return cartItemRepository.save(cart);
    }

    /**
     * Finds a cart item by its ID.
     *
     * @param id The ID of the cart item to be found.
     * @return The found cart item.
     * @throws NoSuchElementException If no cart item is found with the given ID.
     */
    @Override
    public Cart findById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Retrieves all cart items.
     *
     * @return A list of all cart items.
     */
    @Override
    public List<Cart> findAll() {
        return cartItemRepository.findAll();
    }

    /**
     * Deletes a cart item.
     *
     * @param cart The cart item to be deleted.
     * @throws NoSuchElementException If no cart item is found with the given ID.
     */
    @Override
    public void delete(Cart cart) {
        if (cartItemRepository.existsById(cart.getCartId())) {
            cartItemRepository.deleteById(cart.getCartId());
        } else {
            throw new NoSuchElementException("Cart item with id " + cart.getCartId() + " found for delete");
        }
    }

    @Override
    public Cart findCartByUserId(UUID userId, Long cartId) {
        return cartItemRepository.findCartByUserId(userId, cartId);
    }

//    @Override
//    public boolean isCartOwnedByUser(Long cartId, UUID userId) {
//        var cart = findById(cartId);
//        return cart != null && cartItemRepository.findCartByUserId(userId) != null;
//    }
//

}
