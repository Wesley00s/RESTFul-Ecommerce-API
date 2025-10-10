package org.wesley.ecommerce.application.exceptions.local;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(Long cartId) {
        super("Cart with ID " + cartId + " is empty. Please add items to the cart before proceeding.");
    }

}