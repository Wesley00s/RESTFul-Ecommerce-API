package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.CartItemDTO;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Shopping;
import org.wesley.ecommerce.application.service.CartItemService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.ShoppingService;
import org.wesley.ecommerce.application.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/cart-item")
public class CartItemController {
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final UserService userService;
    private final ShoppingService shoppingService;

    public CartItemController(CartItemService cartItemService, ProductService productService, UserService userService, ShoppingService shoppingService) {
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.userService = userService;
        this.shoppingService = shoppingService;
    }

    @PostMapping("/{productId}/{userId}")
    @Operation(summary = "Create a new cart item", description = "Create a new product and return the created cart's data")
    public ResponseEntity<CartItemDTO> createProduct(@RequestParam Integer quantity, @PathVariable Long productId, @PathVariable UUID userId) {
        var product = productService.findById(productId);
        var user = userService.findById(userId);

        var cartItemToCreate = cartItemService.create(new CartItemDTO(product, quantity, user).from());
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cartItemToCreate.getCartId())
                .toUri();
        return ResponseEntity.created(location).body(CartItemDTO.fromDTO(cartItemToCreate));
    }

    @PutMapping("/{productId}/{userId}/{cartId}")
    @Operation(summary = "Update quantity of a cart item", description = "Update the quantity of a cart item in the user's shopping cart")
    public ResponseEntity<CartItemDTO> getProducts(@RequestParam Integer quantity, @PathVariable Long productId, @PathVariable UUID userId, @PathVariable Long cartId) {
        var product = productService.findById(productId);
        var user = userService.findById(userId);
        var cartItemToUpdate = cartItemService.findById(cartId);
        var cartItemToCreate = new CartItemDTO(product, quantity, user).from();
        shoppingService.create(new Shopping(cartItemToUpdate, product, quantity));
        Cart updatedCart = cartItemService.getProducts(cartItemToUpdate.getCartId(), productId, quantity);
        CartItemDTO responseDTO = CartItemDTO.fromDTO(updatedCart);
        return ResponseEntity.ok(responseDTO);
    }
}
