package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.util.List;

public record OrderHistoryDTO(
        Long orderId,
        String orderStatus,
        List<ItemDTO> items,
        Double totalPrice,
        String createdAt
) {

    public static OrderHistoryDTO fromDTO(OrderShopping orderShopping) {
        return new OrderHistoryDTO(
                orderShopping.getId(),
                orderShopping.getStatus().name(),
                orderShopping.getItems().stream()
                        .map(OrderShoppingDTO::toItemDTO).toList(),
                orderShopping.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                        .sum(),
                orderShopping.getCreatedAt().toString()
        );
    }
}
