package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Order;
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
    public Order create(Order order) {
        return orderRepository.save(order);
    }


    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
