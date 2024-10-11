package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Order;

import java.util.List;


@Service
public interface OrderService {
    Order create(Order order);

    Order findById(Long id);
}
