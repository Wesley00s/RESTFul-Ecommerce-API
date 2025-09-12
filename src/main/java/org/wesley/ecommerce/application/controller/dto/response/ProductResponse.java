package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import org.wesley.ecommerce.application.domain.model.Product;

import java.time.LocalDateTime;
import java.util.List;

import static org.wesley.ecommerce.application.controller.dto.request.ProductRequest.getProduct;


public record ProductResponse(
        Long id,
        String name,
        String coverImageUrl,
        List<String> imageUrls,
        String code,
        String description,
        Integer stock,
        ProductCategory category,
        Double price,
        Double rating,
        Integer soldCount,
        Boolean isAvailable,
        LocalDateTime createdAt

) {

    public static ProductResponse fromDTO(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCoverImageUrl(),
                product.getImageUrls(),
                product.getCode(),
                product.getDescription(),
                product.getStock(),
                product.getCategory(),
                product.getPrice(),
                product.getRating(),
                product.getSoldCount(),
                product.getIsAvailable(),
                product.getCreatedAt()
        );
    }

    public Product from(String code) {
        return getProduct(
                this.name,
                this.coverImageUrl,
                this.imageUrls,
                code,
                this.description,
                this.stock,
                this.category,
                this.price
        );
    }
}
