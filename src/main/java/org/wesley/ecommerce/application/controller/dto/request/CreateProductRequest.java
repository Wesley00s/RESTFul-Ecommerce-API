package org.wesley.ecommerce.application.controller.dto.request;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;


public record CreateProductRequest(
        String name,
        String description,
        Integer stock,
        ProductCategory category,
        Double price
) {

    public Product from(String code) {
        var product = new Product();
        product.setName(name());
        product.setDescription(description());
        product.setStock(stock());
        product.setCategory(category());
        product.setPrice(price());
        product.setCode(code);
        return product;
    }
}
