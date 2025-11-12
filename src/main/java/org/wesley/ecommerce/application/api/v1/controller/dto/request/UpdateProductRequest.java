package org.wesley.ecommerce.application.api.v1.controller.dto.request;

import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;


public record UpdateProductRequest(
    String name,
    String description,
    @PositiveOrZero(message = "Stock must be zero or greater")
    Integer stock,
    String categoryName,
    @PositiveOrZero(message = "Price must be zero or greater")
    Double price,
    List<String> publicIdsToDelete
) {}