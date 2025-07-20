package org.wesley.ecommerce.application.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.OrderShopping;

import java.util.List;
import java.util.UUID;


@Service
public interface OrderService {
    OrderShopping create(OrderShopping orderShopping);

    OrderShopping findById(Long id);

    OrderShopping createOrderFromCart(UUID userId);

    OrderShopping confirmOrder(Long orderId, boolean confirm);

    List<OrderShopping> getUserOrderHistory(UUID userId);

    Page<OrderShopping> findAll(Integer page, Integer pageSize);

    OrderShopping update(Long orderId, OrderShopping orderShopping);
}
