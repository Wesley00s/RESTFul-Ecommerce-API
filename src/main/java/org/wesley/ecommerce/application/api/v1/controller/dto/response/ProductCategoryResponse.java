package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import org.wesley.ecommerce.application.domain.model.ProductCategory;


public record ProductCategoryResponse(
        Long id,
        String name,
        String imageUrl,
        String imagePublicId,
        String description
) {

    public static ProductCategoryResponse fromDTO(ProductCategory category) {
        return new ProductCategoryResponse(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                category.getImagePublicId(),
                category.getDescription()
        );
    }
}
