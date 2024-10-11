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
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Item Controller", description = "RESTFul API for managing cart items.")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @Operation(summary = "Create a new cart item", description = "Creates a new cart for the authenticated user.")
    @PostMapping
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

    @Operation(summary = "Add product to cart", description = "Add product to existing cart")
    @PostMapping("/add/{cartId}/{productId}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();
        var user = userService.findById(authenticatedUserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
        }
        var cart = cartService.findById(cartId);
        Product product = new Product();

        productService.findAll().forEach(e -> {
            if(e.getId().equals(productId)) {
                product.setId(e.getId());
                product.setName(e.getName());
                product.setPrice(e.getPrice());
                product.setDescription(e.getDescription());
            }
        });

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cart not found");
        }
        cartService.addToCart(cartId, productId);
        return ResponseEntity.ok("Cart item added.");
    }

    @Operation(summary = "Retrieve a cart by ID", description = "Gets the details of a cart by its ID, for the authenticated user.")
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

    @Operation(summary = "Disable a cart", description = "Disables a cart by its ID for the authenticated user.")
    @PutMapping("/disabled/{cartId}")
    public ResponseEntity<String> disableCartItem(@PathVariable Long cartId) {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        if (cartService.findCartByUserId(authenticatedUserId, cartId) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to this cart.");
        }
        cartService.disableCart(cartId);
        return ResponseEntity.ok("Cart item was disabled.");
    }
}
