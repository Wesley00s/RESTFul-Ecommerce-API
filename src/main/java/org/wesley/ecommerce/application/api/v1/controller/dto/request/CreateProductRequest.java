package org.wesley.ecommerce.application.api.v1.controller.dto.request;


public record CreateProductRequest(
        String name,
        String description,
        Integer stock,
        String categoryName,
        Double price
) {
}
