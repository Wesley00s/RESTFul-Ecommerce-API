package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.response.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.response.PaginationResponse;
import org.wesley.ecommerce.application.controller.dto.request.UserRequest;
import org.wesley.ecommerce.application.controller.dto.response.UserResponse;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

import java.util.UUID;


@RestController
@RequestMapping("/user")
@Tag(name = "Users Controller", description = "RESTFul API for managing users.")
@Data
public class UserController {
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a user by ID",
            description = "Retrieve a specific user based on its ID"
    )
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        var user = userService.findById(id);

        return ResponseEntity.ok(UserResponse.fromUser(user));
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all registered users"
    )
    public ResponseEntity<ApiResponse<UserResponse>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var users = userService.findAll(page, pageSize);
        var usersDTO = users.stream()
                .map(UserResponse::fromUser).toList();

        var pageResponse = new ApiResponse<>(
                usersDTO,
                new PaginationResponse(
                        users.getNumber(),
                        users.getSize(),
                        users.getTotalElements(),
                        users.getTotalPages()
                )
        );
        return ResponseEntity.ofNullable(pageResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remove user",
            description = "Remove user by ID"
    )
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        var userToDelete = userService.findById(id);
        if (userToDelete == null) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(userToDelete);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update a user",
            description = "Update the data of an existing user based on its ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @RequestBody UserRequest userRequest
    ) {

        var updatedUser = userService.update(id, userRequest);
        return ResponseEntity.ok(UserResponse.fromUser(updatedUser));
    }

    @Operation(
            summary = "Find user by email",
            description = "Retrieve a specific user based on its email"
    )
    @GetMapping("/mail/{email}")
    public ResponseEntity<UserResponse> findUserByEmail(@PathVariable String email) {
        var user = userService.findByEmail(email);
        return ResponseEntity.ok(UserResponse.fromUser(user));
    }
}

