package org.wesley.ecommerce.application.api.v1.controller.dto.request;


public record CreateProductCategoryRequest(
        String name,
        String description
) {
}
