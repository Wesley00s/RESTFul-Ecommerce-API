package org.wesley.ecommerce.application.api.v1.controller.dto.request;


public record UpdateProductCategoryRequest(
    String name,
    String description,
    String publicIdToDelete
) {}