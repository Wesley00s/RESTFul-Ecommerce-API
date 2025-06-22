package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;

public record OrderResponse(
        Long orderId,
        String message,
        OrderStatus status
) {
}
