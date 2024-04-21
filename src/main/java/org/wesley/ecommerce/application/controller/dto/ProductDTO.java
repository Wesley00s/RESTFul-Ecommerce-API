package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Product;

import java.math.BigDecimal;

public record ProductDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        String categoryName,
        String categoryDescription
) {

    public static ProductDTO fromDTO(Product product) {
        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategoryName(),
                product.getCategoryDescription()
        );
    }

    public Product from() {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);
        product.setCategoryName(categoryName);
        product.setCategoryDescription(categoryDescription);
        return product;
    }
}
