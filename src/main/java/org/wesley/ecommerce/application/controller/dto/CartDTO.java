package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.User;

import java.util.UUID;


public record CartDTO(
        UUID userId,
        boolean isActive
) {

    public static CartDTO fromDTO(Cart cart) {
        return new CartDTO(
                cart.getUser().getUserId(),
                cart.isActive()
        );
    }

    public Cart from() {
        var cart = new Cart();
        var user = new User();
        cart.setUser(user);
        cart.setActive(isActive);
        user.getCarts().add(cart);
        return cart;
    }
}
