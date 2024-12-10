package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam("quantity") Integer quantity) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        var product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        if (!productService.isStockAvailable(productId, quantity)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not available");
        }

        var existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        var totalPrice = product.getPrice() * quantity;

        if (existingCartItem.isPresent()) {
            var cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            cart.setTotalPrice(cart.getTotalPrice() + totalPrice);
            cartService.update(cart.getId(), cart);
            cart.getItems().forEach(
                    it -> cartItemService.updateTotalPrice(cart.getTotalPrice(),
                            it.getProduct().getId(), cart.getId()));

            return ResponseEntity.ok("Product quantity updated in cart successfully.");
        } else {
            cartItemService.addItemToCart(cart.getId(), productId, quantity, totalPrice);

            cart.setTotalPrice(cart.getTotalPrice() + totalPrice);
            cartService.update(cart.getId(), cart);

            return ResponseEntity.ok("Product added to cart successfully.");
        }
    }

    @Operation(summary = "Remove all products references from cart", description = "Remove all product references from existing cart")
    @DeleteMapping
    public ResponseEntity<String> removeAllProductsReferencesFromCart(
            @RequestParam("product") Long productId) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not have an active cart.");
        }
        var itemPresent = cart.getItems().stream().filter(it -> productId.equals(it.getProduct().getId())).findFirst();
        if (itemPresent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product is not in the cart.");
        }

        var product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        cartItemService.removeAllFromCartItem(cart.getId(), productId);
        cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId()));

        double totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice() * item.getQuantity())
                .reduce(0.0, Double::sum);
        cart.setTotalPrice(totalPrice);

        cartService.update(cart.getId(), cart);

        return ResponseEntity.ok("Product removed from cart successfully.");
    }

    @Operation(summary = "Remove one quantity products references from cart", description = "Remove one quantity product references from existing cart")
    @PutMapping
    public ResponseEntity<String> removeOnlyProductReferencesFromCart(
            @RequestParam("product") Long productId,
            @RequestParam("quantity") int quantity
    ) {
        var user = authenticationService.getAuthenticatedUser();
        var cart = authenticationService.getActiveCart(user);

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not have an active cart.");
        }

        var product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }

        var cartItem = cart.getItems().stream()
                .filter(item -> productId.equals(item.getProduct().getId()))
                .findFirst();

        if (cartItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product is not in the cart.");
        }

        if (cartItem.get().getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot remove more products than are in the cart.");
        }

        cartItemService.removeOnlyFromCartItem(productId, cart.getId(), quantity);
        var item = cartItem.get();
        item.setQuantity(item.getQuantity() - quantity);

        if (item.getQuantity() == 0) {
            cart.getItems().remove(item);
        }

        double newTotalPrice = cart.getItems().stream()
                .mapToDouble(it -> it.getProduct().getPrice() * it.getQuantity())
                .sum();
        cart.setTotalPrice(newTotalPrice);
        cartService.update(cart.getId(), cart);
        cart.getItems().forEach(
                it -> cartItemService.updateTotalPrice(cart.getTotalPrice(),
                        it.getProduct().getId(), cart.getId()));
        return ResponseEntity.ok(quantity + " " + product.getName() + " removed from cart successfully.");
    }
}