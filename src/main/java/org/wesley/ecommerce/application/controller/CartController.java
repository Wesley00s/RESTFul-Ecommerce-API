package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.CartDTO;
import org.wesley.ecommerce.application.exceptions.BusinessException;
import org.wesley.ecommerce.application.exceptions.InsufficientStockException;
import org.wesley.ecommerce.application.service.CartItemService;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.implement.AuthenticationService;

@RestController
@RequestMapping("/cart")
@Data
@Tag(name = "Cart Item Controller", description = "RESTFul API for managing cart items.")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Add product from cart", description = "Add product from existing cart")
    @PostMapping
    public ResponseEntity<String> addProductFromCart(
            @RequestParam("product") Long productId,
            @RequestParam("quantity") Integer quantity
    ) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        try {
            cartService.addProductToCart(cart, productId, quantity);
            return ResponseEntity.ok("Product added to cart successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Remove one quantity products references from cart", description = "Remove one quantity product references from existing cart")
    @PutMapping
    public ResponseEntity<String> removeOnlyProductReferencesFromCart(
            @RequestParam("product") Long productId,
            @RequestParam("quantity") int quantity
    ) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        try {
            cartService.removeProductFromCart(cart, productId, quantity);
            return ResponseEntity.ok(quantity + " product(s) removed from cart successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get authenticated user's cart", description = "Retrieve active cart for the authenticated user.")
    @GetMapping
    public ResponseEntity<CartDTO> getUserCart() {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);
        var activeCart = cartService.findById(cart.getId());
        return ResponseEntity.ok(
                CartDTO.fromDTO(activeCart)
        );
    }

    @Operation(summary = "Toggle item selection in cart", description = "Mark item as pending if selected is true, unselected otherwise")
    @PutMapping("/item/unselect")
    public ResponseEntity<String> toggleItemSelectionInCart(
            @RequestParam("item") Long itemId,
            @RequestParam(value = "selected", defaultValue = "false") boolean selected
    ) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        try {
            cartService.toggleItemSelection(cart, itemId, selected);
            String message = "Item " + (selected ? "selected" : "unselected") + " in cart successfully.";
            return ResponseEntity.ok(message);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}