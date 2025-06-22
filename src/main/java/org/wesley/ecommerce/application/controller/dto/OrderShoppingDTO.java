package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;
import org.wesley.ecommerce.application.domain.model.OrderItem;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderShoppingDTO(
        Long orderId,
        List<ItemDTO> items,
        OrderStatus orderStatus,
        LocalDateTime createdAt

) {
    public static OrderShoppingDTO fromDTO(OrderShopping orderShopping) {
        return new OrderShoppingDTO(
                orderShopping.getId(),
                orderShopping.getItems().stream()
                        .map(OrderShoppingDTO::toItemDTO)
                        .collect(Collectors.toList()),
                orderShopping.getStatus(),
                orderShopping.getCreatedAt()
        );
    }

    static ItemDTO toItemDTO(OrderItem item) {
        return new ItemDTO(
                item.getId(),
                item.getProduct(),
                item.getQuantity(),
                item.getPrice(),
                item.getStatus()
        );
    }
}