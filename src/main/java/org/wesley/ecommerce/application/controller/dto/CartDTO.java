package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.List;


public record CartDTO(
        Long cartID,
        List<ItemDTO> items,
        Double totalPrice
) {

    public static CartDTO fromDTO(Cart cart) {
        return new CartDTO(
                cart.getId(),
                cart.getItems().stream()
                        .map(ItemDTO::fromDTO)
                        .toList(),
                cart.getTotalPrice()
        );
    }

    public Cart from() {
        var cart = new Cart();
        var user = new Users();
        cart.setUser(user);
        return cart;
    }
}
