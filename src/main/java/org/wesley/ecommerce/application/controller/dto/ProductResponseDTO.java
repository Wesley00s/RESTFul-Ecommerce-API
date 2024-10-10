package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.math.BigDecimal;


public record ProductResponseDTO(
        String code,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        ProductCategory category
) {

    public static ProductResponseDTO fromDTO(Product product) {
        return new ProductResponseDTO(
                product.getCod(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory()
        );
    }

    public Product from(String cod) {
        Product product = new Product();
        product.setCod(cod);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setStockQuantity(this.stockQuantity);
        product.setImageUrl(this.imageUrl);
        product.setCategory(this.category);
        return product;
    }
}
