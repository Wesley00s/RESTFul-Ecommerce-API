package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.response.CartResponse;
import org.wesley.ecommerce.application.controller.dto.response.MessageResponse;
import org.wesley.ecommerce.application.service.CartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller", description = "RESTFul API for managing a shopping cart.")
public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add product from cart",
            description = "Add product from existing cart"
    )
    @PostMapping
    public ResponseEntity<MessageResponse> addProductFromCart(
            @RequestParam("product") Long productId,
            @RequestParam("quantity") Integer quantity
    ) {
        cartService.addProductToCart(productId, quantity);
        return ResponseEntity.ok(new MessageResponse("Product added to cart successfully."));
    }

    @Operation(
            summary = "Remove one quantity products references from cart",
            description = "Remove one quantity product references from existing cart"
    )
    @PutMapping
    public ResponseEntity<MessageResponse> removeOnlyProductReferencesFromCart(
            @RequestParam("product") Long productId,
            @RequestParam("quantity") Integer quantity
    ) {
        cartService.removeProductFromCart(productId, quantity);
        return ResponseEntity.ok(new MessageResponse(quantity + " product(s) removed from cart successfully."));
    }

    @Operation(
            summary = "Get authenticated user's cart",
            description = "Retrieve active cart for the authenticated user."
    )
    @GetMapping
    public ResponseEntity<CartResponse> getUserCart() {
        var activeCart = cartService.currentCart();
        return ResponseEntity.ok(CartResponse.fromDTO(activeCart));
    }

    @Operation(
            summary = "Toggle item selection in cart",
            description = "Mark item as pending if selected is true, unselected otherwise"
    )
    @PutMapping("/item/unselect")
    public ResponseEntity<MessageResponse> toggleItemSelectionInCart(
            @RequestParam("item") Long itemId,
            @RequestParam(value = "selected", defaultValue = "false") Boolean selected
    ) {
        var isSelected = cartService.toggleItemSelection(itemId, selected);
        return ResponseEntity.ok(new MessageResponse("Item " + (isSelected ? "selected" : "unselected") + " in cart successfully."));
    }
}