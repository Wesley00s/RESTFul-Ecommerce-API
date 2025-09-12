package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.response.OrderHistoryResponse;
import org.wesley.ecommerce.application.controller.dto.response.OrderShoppingResponse;
import org.wesley.ecommerce.application.controller.dto.response.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.response.PaginationResponse;
import org.wesley.ecommerce.application.domain.model.*;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;
import org.wesley.ecommerce.application.service.implement.AuthenticationService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Data
@Tag(name = "OrderShopping Controller", description = "RESTful API for managing shopping.")
class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Create a new order",
            description = "Create a new order and send it for admin confirmation."
    )
    public ResponseEntity<OrderShoppingResponse> createOrder() {
        Users user = authenticationService.getAuthenticatedUser();
        OrderShopping order = orderService.createOrderFromCart(user.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.created(location).body(OrderShoppingResponse.fromDTO(order));
    }

    @PostMapping("/admin/confirm")
    @Transactional
    @Operation(
            summary = "Confirm or reject an order",
            description = "Allows an admin to confirm or reject a placed order."
    )
    public ResponseEntity<OrderShoppingResponse> confirmOrder(@RequestParam("order") Long orderId, @RequestParam boolean confirm) {
        OrderShopping order = orderService.confirmOrder(orderId, confirm);
        return ResponseEntity.ok(OrderShoppingResponse.fromDTO(order));
    }

    @GetMapping("/admin/all")
    @Operation(
            summary = "Retrieve all orders",
            description = "Returns a list of all orders."
    )
    public ResponseEntity<ApiResponse<OrderShoppingResponse>> getAllOrders(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var orders = orderService.findAll(page, pageSize);
        var orderList = orders.stream()
                .map(OrderShoppingResponse::fromDTO)
                .collect(Collectors.toList());
        var pageResponse = new ApiResponse<>(
                orderList,
                new PaginationResponse(orders.getNumber(), orders.getSize(), orders.getTotalElements(), orders.getTotalPages())
        );
        return ResponseEntity.ok(
                pageResponse
        );
    }

    @GetMapping("/history")
    @Operation(
            summary = "Retrieve user order history",
            description = "Returns a list of all orders for the authenticated user."
    )
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistory() {
        Users user = authenticationService.getAuthenticatedUser();
        List<OrderShopping> orders = orderService.getUserOrderHistory(user.getId());

        return ResponseEntity.ok(orders.stream()
                .map(OrderHistoryResponse::fromDTO)
                .collect(Collectors.toList()));
    }
}