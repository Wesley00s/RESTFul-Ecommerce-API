package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.CartItem;
import org.wesley.ecommerce.application.domain.model.Product;

public record ItemResponse(
        Long itemID,
        Product product,
        Integer quantity,
        Double price,
        ItemStatus status
) {
    public static ItemResponse fromDTO(CartItem cartItem) {
        return new ItemResponse(
                cartItem.getId(),
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getPrice(),
                cartItem.getStatus()
        );
    }
}
