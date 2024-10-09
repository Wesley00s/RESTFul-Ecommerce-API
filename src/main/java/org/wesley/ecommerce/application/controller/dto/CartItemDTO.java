package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.User;

import java.util.UUID;

/**
 * A data transfer object (DTO) representing a single item in a shopping cart.
 * This DTO contains information about the user associated with the cart item.

 * @param userId The user who owns the cart item.
 */
public record CartItemDTO(
        UUID userId
) {

    /**
     * Creates a new {@link CartItemDTO} instance from a given {@link Cart} object.
     *
     * @param cart The {@link Cart} object to create the DTO from.
     * @return A new {@link CartItemDTO} instance with the same user as the given cart.
     */
    public static CartItemDTO fromDTO(Cart cart) {
        return new CartItemDTO(
                cart.getUser().getUserId()
        );
    }

    /**
     * Creates a new {@link Cart} object from the current {@link CartItemDTO} instance.
     *
     * @return A new {@link Cart} object with the same user as the current DTO.
     */
    public Cart from() {
        var cart = new Cart();
        var user = new User();
        cart.setUser(user);
        user.getCarts().add(cart);
        return cart;
    }
}
