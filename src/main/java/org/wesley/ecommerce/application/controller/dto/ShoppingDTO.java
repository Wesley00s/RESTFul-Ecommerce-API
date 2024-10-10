package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Shopping;

import java.time.LocalDateTime;

public record ShoppingDTO(
        Long cartId,
        Long productId,
        Integer quantity,
        LocalDateTime createAt
) {
    public static ShoppingDTO fromDTO(Shopping shopping) {
        return new ShoppingDTO(
                shopping.getCart().getCartId(),
                shopping.getProduct().getProductId(),
                shopping.getQuantity(),
                shopping.getCreatedAt()
        );
    }

    public Shopping toShopping() {
        var shopping = new Shopping();
        var cart = new Cart();
        var product = new Product();
        cart.setCartId(this.cartId);
        product.setProductId(this.productId);
        shopping.setCart(cart);
        shopping.setProduct(product);
        shopping.setQuantity(this.quantity);
        shopping.setCreatedAt(this.createAt);
        return shopping;
    }
}
