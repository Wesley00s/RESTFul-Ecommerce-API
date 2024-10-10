package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.math.BigDecimal;


public record ProductRequestDTO(
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        ProductCategory category
) {

    public static ProductRequestDTO fromDTO(Product product) {
        return new ProductRequestDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory()
        );
    }

    static Product getProduct(String cod, String name, String description, BigDecimal price, Integer stockQuantity, String imageUrl, ProductCategory category) {
        Product product = new Product();
        product.setCod(cod);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        return product;
    }

    public Product from(String cod) {
        return getProduct(cod, this.name, this.description, this.price, this.stockQuantity, this.imageUrl, this.category);
    }
}
