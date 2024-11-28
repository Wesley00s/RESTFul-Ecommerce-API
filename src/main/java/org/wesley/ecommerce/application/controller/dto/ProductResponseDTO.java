package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import static org.wesley.ecommerce.application.controller.dto.ProductRequestDTO.getProduct;


public record ProductResponseDTO(
        String name,
        String imageUrl,
        String code,
        String description,
        Integer stock,
        ProductCategory category,
        Double price
) {

    public static ProductResponseDTO fromDTO(Product product) {
        return new ProductResponseDTO(
                product.getName(),
                product.getImageUrl(),
                product.getCode(),
                product.getDescription(),
                product.getStock(),
                product.getCategory(),
                product.getPrice()
        );
    }

    public Product from(String code) {
        return getProduct(
                this.name,
                this.imageUrl,
                code,
                this.description,
                this.stock,
                this.category,
                this.price
        );
    }
}
