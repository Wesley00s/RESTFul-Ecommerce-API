package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.math.BigDecimal;

import static org.wesley.ecommerce.application.controller.dto.ProductRequestDTO.getProduct;


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
        return getProduct(cod, this.name, this.description, this.price, this.stockQuantity, this.imageUrl, this.category);
    }
}
