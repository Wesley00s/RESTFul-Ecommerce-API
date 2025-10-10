package org.wesley.ecommerce.application.api.v1.controller.dto.event;

public record ProductRatingUpdatedEvent(
    Double newRating,
    Integer totalReviews,
    String productCode
) {
}