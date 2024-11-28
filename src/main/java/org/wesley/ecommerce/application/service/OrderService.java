package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.OrderShopping;


@Service
public interface OrderService {
    OrderShopping create(OrderShopping orderShopping);

    OrderShopping findById(Long id);
}
