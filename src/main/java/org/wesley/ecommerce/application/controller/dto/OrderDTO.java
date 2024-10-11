package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Order;
import org.wesley.ecommerce.application.domain.model.Product;

import java.util.List;

public record OrderDTO(
        Long cartId,
        Long productId
) {

}
