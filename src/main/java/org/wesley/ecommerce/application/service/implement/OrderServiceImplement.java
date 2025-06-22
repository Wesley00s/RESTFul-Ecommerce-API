package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.enumeration.ItemStatus;
import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.OrderItem;
import org.wesley.ecommerce.application.domain.model.OrderShopping;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.repository.OrderRepository;
import org.wesley.ecommerce.application.exceptions.CartEmptyException;
import org.wesley.ecommerce.application.exceptions.InsufficientStockException;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;


    public OrderServiceImplement(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
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
    @Transactional
    public OrderShopping createOrderFromCart(UUID userId) {
        Cart cart = cartService.findCartByUserId(userId);

        OrderShopping order = new OrderShopping();
        order.setUser(cart.getUser());

        List<OrderItem> orderItems = cart.getItems().stream()
                .filter(item -> item.getStatus() == ItemStatus.PENDING)
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    orderItem.setStatus(ItemStatus.PENDING);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());
        if (orderItems.isEmpty()) {
            throw new CartEmptyException(cart.getId());
        }

        order.setItems(orderItems);

        cart.getItems().removeIf(item -> item.getStatus() == ItemStatus.PENDING);
        cart.recalculateTotal();
        cartService.create(cart);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderShopping confirmOrder(Long orderId, boolean confirm) {
        OrderShopping order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus().equals(OrderStatus.COMPLETED)) {
            throw new IllegalStateException("This order aleready has a status of " + order.getStatus());
        }

        if (confirm) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                if (product.getStock() < item.getQuantity()) {
                    throw new InsufficientStockException(product.getName(), product.getId());
                }
                product.setStock(product.getStock() - item.getQuantity());
            }

            order.setStatus(OrderStatus.COMPLETED);
            order.getItems().forEach(item -> item.setStatus(ItemStatus.COMPLETED));
        } else {
            order.setStatus(OrderStatus.CANCELLED);
            order.getItems().forEach(item -> item.setStatus(ItemStatus.CANCELLED));
        }

        return orderRepository.save(order);
    }

    @Override
    public List<OrderShopping> getUserOrderHistory(UUID userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<OrderShopping> findAll() {
        return orderRepository.findAllWithItems();
    }

    @Override
    public OrderShopping update(Long orderId, OrderShopping orderShopping) {
        return orderRepository.findById(orderId)
                .map(existingOrder -> {
                    if (orderShopping.getStatus() != null) {
                        existingOrder.setStatus(orderShopping.getStatus());
                    }

                    if (orderShopping.getItems() != null && !orderShopping.getItems().isEmpty()) {
                        existingOrder.getItems().clear();
                        existingOrder.getItems().addAll(orderShopping.getItems());
                        existingOrder.getItems().forEach(item -> item.setOrder(existingOrder));
                    }

                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new NoSuchElementException("Order not found for update"));
    }
}