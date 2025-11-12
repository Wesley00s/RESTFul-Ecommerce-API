package org.wesley.ecommerce.application.exceptions.local;


public class ProductCategoryNotFoundException extends RuntimeException {
    public ProductCategoryNotFoundException(String categoryName) {
        super("Product category " + categoryName + " not found");
    }

}