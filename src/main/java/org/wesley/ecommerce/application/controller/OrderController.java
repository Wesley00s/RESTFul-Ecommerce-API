package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;
import org.wesley.ecommerce.application.domain.model.OrderShopping;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/order")
@Data
@Tag(name = "OrderShopping Controller", description = "RESTful API for managing shopping.")
class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    @Transactional
    @Operation(summary = "Create a new order", description = "Create a new order and send it for admin confirmation.")
    public ResponseEntity<String> createOrder() {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getId();

        var cart = cartService.findCartByUserId(authenticatedUserId);

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this cart.");
        }

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty. Cannot create an order.");
        }

        for (var item : cart.getItems()) {
            var product = productService.findById(item.getProduct().getId());
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found: " + item.getProduct().getId());
            }

            if (product.getStock() < item.getQuantity()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Insufficient stock for product: " + product.getName());
            }
        }

        var order = new OrderShopping();
        order.setCart(cart);
        order.setCreatedAt(LocalDateTime.now());
        orderService.create(order);

        return ResponseEntity.ok("Order created successfully and sent for admin confirmation.");
    }

    @PostMapping("/admin/confirm")
    @Transactional
    @Operation(summary = "Confirm or reject an order", description = "Allows an admin to confirm or reject a placed order.")
    public ResponseEntity<String> confirmOrder(@RequestParam("order") Long orderId, @RequestParam boolean confirm) {
        var order = orderService.findById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }

        var cart = order.getCart();
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is empty. Cannot confirm an order.");
        }

        if (confirm) {
            for (var item : cart.getItems()) {
                var product = productService.findById(item.getProduct().getId());
                if (product == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found: " + item.getProduct().getId());
                }

                var currentStock = product.getStock();
                if (currentStock < item.getQuantity()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Insufficient stock for product: " + product.getName());
                }

                productService.updateStock(product.getId(), currentStock - item.getQuantity());
            }

            order.setStatus(OrderStatus.COMPLETED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
        cart.setTotalPrice(0.0);

        cart.setItems(new ArrayList<>(cart.getItems()));
        cart.getItems().clear();

        cartService.update(cart.getId(), cart);
        orderService.update(order.getId(), order);

        return ResponseEntity.ok("Order " + (confirm ? "confirmed" : "cancelled") + " successfully.");
    }
}