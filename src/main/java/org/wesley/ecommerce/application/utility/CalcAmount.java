package org.wesley.ecommerce.application.utility;

import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.model.Cart;

public class CalcAmount {
    public static void recalculateCartTotal(Cart cart) {
        double newTotalPrice = cart.getItems().stream()
                .filter(item -> item.getStatus().equals(ItemStatus.PENDING))
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(newTotalPrice);
    }
}
