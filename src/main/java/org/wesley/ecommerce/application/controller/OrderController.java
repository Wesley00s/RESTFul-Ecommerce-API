package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wesley.ecommerce.application.controller.dto.OrderDTO;
import org.wesley.ecommerce.application.domain.model.Order;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "RESTful API for managing shopping.")
class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    @Transactional
    @Operation(summary = "Create a new order", description = "Create a new order, validate stock quantity and return a message with status.")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO) {
        var cart = cartService.findById(orderDTO.cartId());
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        if (cartService.findCartByUserId(authenticatedUserId, cart.getId()) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this cart.");
        }

        Product product;
        try {
            List<Product> productsInCart = productService.findProductsByCart(cart.getId());
            product = productsInCart.stream()
                    .filter(p -> p.getId().equals(orderDTO.productId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("This product does not exist in the cart."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        int currentStock = product.getStockQuantity();
        if (currentStock <= 0) {
            return ResponseEntity.badRequest().body("Not enough stock available.");
        }

        var order = new Order();
        order.setCart(cart);
        order.setCreatedAt(LocalDateTime.now());

        productService.updateQuantityStock(product.getId(), currentStock - 1);
        orderService.create(order);

        return ResponseEntity.ok("Order created successfully.");
    }
}
