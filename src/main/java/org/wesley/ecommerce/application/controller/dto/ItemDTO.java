package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.model.Product;

public record ItemDTO(
        Long itemID,
        Product product,
        Integer quantity,
        Double price,
        ItemStatus status
) {
    public static ItemDTO fromDTO(CartItem cartItem) {
        return new ItemDTO(
                cartItem.getId(),
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getStatus()
        );
    }
}
