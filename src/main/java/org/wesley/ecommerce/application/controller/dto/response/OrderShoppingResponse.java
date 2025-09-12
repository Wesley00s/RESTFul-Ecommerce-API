package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;
import org.wesley.ecommerce.application.domain.model.OrderItem;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderShoppingResponse(
        Long orderId,
        List<ItemResponse> items,
        OrderStatus orderStatus,
        LocalDateTime createdAt

) {
    public static OrderShoppingResponse fromDTO(OrderShopping orderShopping) {
        return new OrderShoppingResponse(
                orderShopping.getId(),
                orderShopping.getItems().stream()
                        .map(OrderShoppingResponse::toItemDTO)
                        .collect(Collectors.toList()),
                orderShopping.getStatus(),
                orderShopping.getCreatedAt()
        );
    }

    public static ItemResponse toItemDTO(OrderItem item) {
        return new ItemResponse(
                item.getId(),
                item.getProduct(),
                item.getQuantity(),
                item.getPrice(),
                item.getStatus()
        );
    }
}