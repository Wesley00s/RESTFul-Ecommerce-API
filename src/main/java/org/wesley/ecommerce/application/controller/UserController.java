package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.service.CartItemService;
import org.wesley.ecommerce.application.service.UserService;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce/user")
@Tag(name = "Users Controller", description = "RESTFul API for managing users.")
public class UserController {
    private final UserService userService;
    private final CartItemService cartItemService;

    public UserController(UserService userService, CartItemService cartItemService) {
        this.userService = userService;
        this.cartItemService = cartItemService;
    }

    @Transactional
    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user and return the created user's data")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        var userCreated = userService.create(user.from());

        var emptyCartItem = cartItemService.create(new Cart(new Random().nextLong(), null, 0, userCreated));

        userCreated.setCart(emptyCartItem);
        userService.update(userCreated.getUserId(), userCreated);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userCreated.getUserId())
                .toUri();
        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieve a specific user based on its ID")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        var user = userService.findById(id);
        var userDTO = new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.getAddress().getStreet(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getZip()
        );
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = userService.findAll();
        var usersDTO = users.stream()
                .map(user -> new UserDTO(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserType(),
                        user.getAddress().getStreet(),
                        user.getAddress().getCity(),
                        user.getAddress().getState(),
                        user.getAddress().getZip()
                )).toList();
        return ResponseEntity.ofNullable(usersDTO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Remove user", description = "Remove user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        var userToDelete = userService.findById(id);
        userService.delete(userToDelete);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.update(id, userDTO.from());
        return ResponseEntity.ok(UserDTO.fromUser(updatedUser));
    }
}

