package org.wesley.ecommerce.application.api.v1.controller.dto.event;

import java.util.UUID;

public record ReviewCreatedEvent(
    String reviewId,
    Long productId,
    String productCode,
    UUID customerId,
    String customerName,
    Double rating,
    String content
) {}