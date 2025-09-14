package org.wesley.ecommerce.application.controller.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import org.wesley.ecommerce.application.domain.enumeration.ProductCategory;
import java.util.List;


public record UpdateProductRequest(
    String name,
    String description,
    @PositiveOrZero(message = "Stock must be zero or greater")
    Integer stock,
    ProductCategory category,
    @PositiveOrZero(message = "Price must be zero or greater")
    Double price,
    List<String> publicIdsToDelete
) {}