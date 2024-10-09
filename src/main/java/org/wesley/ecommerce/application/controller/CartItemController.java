package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.CartItemDTO;
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
    private final ProductService productService;
    private final UserService userService;
    private final ShoppingService shoppingService;

    @PostMapping
    @Operation()
    public ResponseEntity<String> createCartItem() {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        var user = userService.findById(authenticatedUserId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var cart = new Cart();
        cart.setUser(user);
        cartService.create(cart);

        return ResponseEntity.ok("Cart item created.");
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<String> getCartItem(@PathVariable Long cartId) {
        var authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var authenticatedUserId = userService.findByEmail(authenticatedUser.getUsername()).getUserId();

        var cart = cartService.findById(cartId);
        if (cartService.findCartByUserId(authenticatedUserId, cartId) == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have a access at this cart.");

        }
        return ResponseEntity.ok(authenticatedUserId.toString());
    }


//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<CartItemDTO> deleteCartItem(@PathVariable UUID id) {
//        var
//    }


//    @PostMapping("/{productId}/{userId}")
//    @Operation(summary = "Create a new cart item", description = "Create a new product and return the created cart's data")
//    public ResponseEntity<CartItemDTO> createProduct(@RequestParam Integer quantity, @PathVariable Long productId, @PathVariable UUID userId) {
//        var product = productService.findById(productId);
//        var user = userService.findById(userId);
//
//        var cartItemToCreate = cartItemService.create(new CartItemDTO(product, quantity, user).from());
//        var location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(cartItemToCreate.getCartId())
//                .toUri();
//        return ResponseEntity.created(location).body(CartItemDTO.fromDTO(cartItemToCreate));
//    }
//
//    @PutMapping("/{productId}/{userId}/{cartId}")
//    @Operation(summary = "Update quantity of a cart item", description = "Update the quantity of a cart item in the user's shopping cart")
//    public ResponseEntity<CartItemDTO> getProducts(@RequestParam Integer quantity, @PathVariable Long productId, @PathVariable UUID userId, @PathVariable Long cartId) {
//        var products = productService.findAll();
//        var user = userService.findById(userId);
//        var cartItemToUpdate = cartItemService.findById(cartId);
//        var cartItemToCreate = new CartItemDTO(products).from();
//        shoppingService.create(new Shopping(cartItemToUpdate, products, quantity));
//        Cart updatedCart = cartItemService.getProducts(cartItemToUpdate.getCartId(), productId, quantity);
//        CartItemDTO responseDTO = CartItemDTO.fromDTO(updatedCart);
//        return ResponseEntity.ok(responseDTO);
//    }
}
