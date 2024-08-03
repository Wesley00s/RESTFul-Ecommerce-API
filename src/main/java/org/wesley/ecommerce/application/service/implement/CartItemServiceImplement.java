package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.repository.CartItemRepository;
import org.wesley.ecommerce.application.domain.repository.ProductRepository;
import org.wesley.ecommerce.application.service.CartItemService;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class implements the CartItemService interface and provides methods for managing cart items.
 * It uses Spring's @Service annotation to mark it as a service component.
 */
@Service
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    /**
     * Constructor for CartItemServiceImplement.
     *
     * @param cartItemRepository The repository for managing cart items.
     * @param productRepository The repository for managing products.
     */
    public CartItemServiceImplement(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

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
        if(cartItemRepository.existsById(cart.getCartId())){
            cartItemRepository.deleteById(cart.getCartId());
        } else {
            throw new NoSuchElementException("Cart item with id " + cart.getCartId() + " found for delete");
        }
    }

    /**
     * Adds products to a cart item.
     *
     * @param id The ID of the cart item.
     * @param productId The ID of the product to be added.
     * @param quantity The quantity of the product to be added.
     * @return The updated cart item.
     * @throws NoSuchElementException If no cart item or product is found with the given IDs.
     * @throws IllegalArgumentException If the requested quantity exceeds the available stock.
     */
    @Override
    public Cart getProducts(Long id, Long productId, Integer quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + productId + " not found"));
        var cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cart item not found for ID: " + id));
        var currentStock = product.getStockQuantity();

        if (product.getStockQuantity() < quantity) {
            product.setStockQuantity(currentStock - quantity); ;
        } else {
            throw new IllegalArgumentException("Requested quantity exceeds available stock.");
        }
        return cartItemRepository.save(cartItem);
    }

}
