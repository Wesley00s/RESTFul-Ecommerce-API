package org.wesley.ecommerce.application.exceptions.local;


public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, Long productId) {
        super("Insufficient stock for product: " + productName + " (ID: " + productId + ")");
    }

}