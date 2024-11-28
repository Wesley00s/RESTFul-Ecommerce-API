package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.List;
import java.util.UUID;


public record CartDTO(
        UUID userId,
        List<CartItem> items,
        Double totalPrice
) {

    public static CartDTO fromDTO(Cart cart) {
        return new CartDTO(
                cart.getUsers().getId(),
                cart.getItems(),
                cart.getTotalPrice()
        );
    }

    public Cart from() {
        var cart = new Cart();
        var user = new Users();
        cart.setUsers(user);
        return cart;
    }
}
