package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.User;

public record CartItemDTO(
        Product product,
        Integer quantity,
        User user
) {

    public static CartItemDTO fromDTO(Cart cart) {
        return new CartItemDTO(
                cart.getProduct(),
                cart.getQuantity(),
                cart.getUser()
        );
    }

    public Cart from() {
        var cartItem = new Cart();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUser(user);
        return cartItem;
    }
}
