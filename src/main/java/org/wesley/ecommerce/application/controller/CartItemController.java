package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.CartDTO;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.ShoppingService;
import org.wesley.ecommerce.application.service.UserService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Item Controller", description = "RESTFul API for managing cart items.")
public class CartItemController {
    private final CartService cartService;
    private final UserService userService;

    @PostMapping
    @Operation()
    public ResponseEntity<String> createCartItem() {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        var user = userService.findById(authenticatedUserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }

        var cart = new Cart();
        cart.setUser(user);
        cart.setActive(true);
        cartService.create(cart);

        return ResponseEntity.ok("Cart item created.");
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCartItem(@PathVariable Long cartId) {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        if (cartService.findCartByUserId(authenticatedUserId, cartId) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        Cart cart = cartService.findCartByUserId(authenticatedUserId, cartId);

        return ResponseEntity.ok(new CartDTO(authenticatedUserId, cart.isActive()));
    }

    @PutMapping("/disabled/{cartId}")
    public ResponseEntity<String> disableCartItem(@PathVariable Long cartId) {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        if (cartService.findCartByUserId(authenticatedUserId, cartId) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have a access at this cart.");
        }
        var cart = cartService.findCartByUserId(authenticatedUserId, cartId);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cart does not exist.");
        }
        cartService.disableCart(cartId);
        return ResponseEntity.ok("Cart item was disabled.");
    }
}
