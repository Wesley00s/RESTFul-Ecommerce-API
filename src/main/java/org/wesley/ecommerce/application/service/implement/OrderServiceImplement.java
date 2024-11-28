package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.OrderShopping;
import org.wesley.ecommerce.application.domain.repository.OrderRepository;
import org.wesley.ecommerce.application.service.OrderService;

import java.util.NoSuchElementException;

@Service
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImplement(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderShopping create(OrderShopping orderShopping) {
        return orderRepository.save(orderShopping);
    }


    @Override
    public OrderShopping findById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
