package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Shopping;

public record ShoppingDTO(
        Long cartId,
        Long productId,
        Integer quantity
) {
    public static ShoppingDTO fromDTO(Shopping shopping) {
        return new ShoppingDTO(
                shopping.getCart().getCartId(),
                shopping.getProduct().getProductId(),
                shopping.getQuantity()
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
        return shopping;
    }
}
