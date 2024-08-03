package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.User;

/**
 * A data transfer object (DTO) representing a single item in a shopping cart.
 * This DTO contains information about the product, quantity, and user associated with the cart item.
 *
 * @param product The product associated with the cart item.
 * @param quantity The quantity of the product in the cart.
 * @param user The user who owns the cart item.
 */
public record CartItemDTO(
        Product product,
        Integer quantity,
        User user
) {

    /**
     * Creates a new {@link CartItemDTO} instance from a given {@link Cart} object.
     *
     * @param cart The {@link Cart} object to create the DTO from.
     * @return A new {@link CartItemDTO} instance with the same product, quantity, and user as the given cart.
     */
    public static CartItemDTO fromDTO(Cart cart) {
        return new CartItemDTO(
                cart.getProduct(),
                cart.getQuantity(),
                cart.getUser()
        );
    }

    /**
     * Creates a new {@link Cart} object from the current {@link CartItemDTO} instance.
     *
     * @return A new {@link Cart} object with the same product, quantity, and user as the current DTO.
     */
    public Cart from() {
        var cartItem = new Cart();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUser(user);
        return cartItem;
    }
}
