package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import org.wesley.ecommerce.application.domain.model.Product;

import java.time.LocalDateTime;
import java.util.Map;


public record ProductResponse(
        Long id,
        String name,
        String coverImageUrl,
        String coverImagePublicId,
        Map<String, String> imageUrls,
        String code,
        String description,
        Integer stock,
        String categoryName,
        Double price,
        Double rating,
        Long totalReviews,
        Integer soldCount,
        Boolean isAvailable,
        LocalDateTime createdAt

) {

    public static ProductResponse fromDTO(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCoverImageUrl(),
                product.getCoverImagePublicId(),
                product.getImageUrls(),
                product.getCode(),
                product.getDescription(),
                product.getStock(),
                product.getCategory().getName(),
                product.getPrice(),
                product.getRating(),
                product.getTotalReviews(),
                product.getSoldCount(),
                product.getIsAvailable(),
                product.getCreatedAt()
        );
    }
}
