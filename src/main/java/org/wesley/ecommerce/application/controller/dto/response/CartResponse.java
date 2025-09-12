package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.List;


public record CartResponse(
        Long cartID,
        List<ItemResponse> items,
        Double totalPrice
) {

    public static CartResponse fromDTO(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getItems().stream()
                        .map(ItemResponse::fromDTO)
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
