package org.wesley.ecommerce.application.controller;

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
import org.wesley.ecommerce.application.controller.dto.ShoppingDTO;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.ShoppingService;
import org.wesley.ecommerce.application.service.UserService;


@RestController
@RequestMapping("/shopping")
@RequiredArgsConstructor
class ShoppingController {
    private final ShoppingService shoppingService;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> createShopping(@RequestBody ShoppingDTO shoppingDTO) {
        var cart = cartService.findById(shoppingDTO.cartId());
        var product = productService.findById(shoppingDTO.productId());

        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        var cartToService = cartService.findById(cart.getCartId());
        if (cartService.findCartByUserId(authenticatedUserId, cartToService.getCartId()) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this cart.");
        }

        var currentStock = product.getStockQuantity();
        if (shoppingDTO.quantity() > currentStock) {
            return ResponseEntity.badRequest().body("Not enough stock available.");
        }

        productService.updateQuantityStock(shoppingDTO.productId(), currentStock - shoppingDTO.quantity());

        shoppingService.create(shoppingDTO.toShopping());
        return ResponseEntity.ok("Shopping created successfully.");
    }
}
