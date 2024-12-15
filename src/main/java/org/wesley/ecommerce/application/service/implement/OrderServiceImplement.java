package org.wesley.ecommerce.application.service.implement;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.OrderShopping;
import org.wesley.ecommerce.application.domain.repository.OrderRepository;
import org.wesley.ecommerce.application.service.OrderService;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Override
    public void update(Long orderId, OrderShopping orderShopping) {
        Optional<OrderShopping> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            var existingOrder = order.get();
            if (existingOrder.getStatus() != null) {
                existingOrder.setStatus(orderShopping.getStatus());
            }
            if (orderShopping.getId() != null) {
                existingOrder.setId(orderShopping.getId());
            }
            if (orderShopping.getCart() != null) {
                existingOrder.setCart(orderShopping.getCart());
            }
            if (orderShopping.getCreatedAt() != null) {
                existingOrder.setCreatedAt(orderShopping.getCreatedAt());
            }
        } else {
            throw new NoSuchElementException("Order not found for update");
        }
    }
}
