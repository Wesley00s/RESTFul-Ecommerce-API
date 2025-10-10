package org.wesley.ecommerce.application.exceptions.local;


public class ProductCategoryNotFoundException extends RuntimeException {
    public ProductCategoryNotFoundException(Long categoryId) {
        super("Product category with id " + categoryId + " not found");
    }

}