package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.util.List;

public record OrderHistoryResponse(
        Long orderId,
        String orderStatus,
        List<ItemResponse> items,
        Double totalPrice,
        String createdAt
) {

    public static OrderHistoryResponse fromDTO(OrderShopping orderShopping) {
        List<ItemResponse> itemResponses = orderShopping.getItems().stream()
                .map(OrderShoppingResponse::toItemDTO)
                .toList();

        return new OrderHistoryResponse(
                orderShopping.getId(),
                orderShopping.getStatus().name(),
                itemResponses,
                itemResponses.stream()
                        .mapToDouble(ItemResponse::price)
                        .sum(),
                orderShopping.getCreatedAt().toString()
        );
    }
}
