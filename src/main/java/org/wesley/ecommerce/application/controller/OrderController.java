package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.OrderHistoryDTO;
import org.wesley.ecommerce.application.controller.dto.OrderShoppingDTO;
import org.wesley.ecommerce.application.controller.dto.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.PaginationResponse;
import org.wesley.ecommerce.application.domain.model.*;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;
import org.wesley.ecommerce.application.service.implement.AuthenticationService;

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
    @Operation(summary = "Create a new order", description = "Create a new order and send it for admin confirmation.")
    public ResponseEntity<OrderShoppingDTO> createOrder() {
        Users user = authenticationService.getAuthenticatedUser();
        OrderShopping order = orderService.createOrderFromCart(user.getId());
        return ResponseEntity.ok(OrderShoppingDTO.fromDTO(order));
    }

    @PostMapping("/admin/confirm")
    @Transactional
    @Operation(summary = "Confirm or reject an order", description = "Allows an admin to confirm or reject a placed order.")
    public ResponseEntity<OrderShoppingDTO> confirmOrder(@RequestParam("order") Long orderId, @RequestParam boolean confirm) {
        OrderShopping order = orderService.confirmOrder(orderId, confirm);
        return ResponseEntity.ok(OrderShoppingDTO.fromDTO(order));
    }

    @GetMapping("/admin/all")
    @Operation(summary = "Retrieve all orders", description = "Returns a list of all orders.")
    public ResponseEntity<ApiResponse<OrderShoppingDTO>> getAllOrders(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var orders = orderService.findAll(page, pageSize);
        var orderList = orders.stream()
                .map(OrderShoppingDTO::fromDTO)
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
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistory() {
        Users user = authenticationService.getAuthenticatedUser();
        List<OrderShopping> orders = orderService.getUserOrderHistory(user.getId());

        return ResponseEntity.ok(orders.stream()
                .map(OrderHistoryDTO::fromDTO)
                .collect(Collectors.toList()));
    }
}