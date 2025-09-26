package org.wesley.ecommerce.application.api.v1.controller.dto.request;

import jakarta.validation.constraints.*;

public record CreateReviewRequest(
    @NotNull @Min(1) @Max(5)
    Double rating,
    @NotBlank @Size(max = 1000)
    String content
) {}