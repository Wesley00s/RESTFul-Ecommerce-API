package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.ApiResponse;
import org.wesley.ecommerce.application.controller.dto.PaginationResponse;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.controller.dto.UserResponseDTO;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/user")
@Tag(name = "Users Controller", description = "RESTFul API for managing users.")
@Data
public class UserController {
    private final UserService userService;
    private final CartService cartService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Retrieve a specific user based on its ID")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        var user = userService.findById(id);

        var userDTO = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.getAddress().getStreet(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getZip(),
                user.getCreatedAt()
        );

        return ResponseEntity.ok(userDTO);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var users = userService.findAll(page, pageSize);
        var usersDTO = users.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserType(),
                        user.getAddress().getStreet(),
                        user.getAddress().getCity(),
                        user.getAddress().getState(),
                        user.getAddress().getZip(),
                        user.getCreatedAt()
                )).toList();

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
    @Operation(summary = "Remove user", description = "Remove user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        var userToDelete = userService.findById(id);
        if (userToDelete == null) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(userToDelete);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        var user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        var updatedUser = userService.update(id, userDTO);
        return ResponseEntity.ok(UserResponseDTO.fromUser(updatedUser));
    }

    @Operation(summary = "Find user by email", description = "Retrieve a specific user based on its email")
    @GetMapping("/mail/{email}")
    public ResponseEntity<UserResponseDTO> findUserByEmail(@PathVariable String email) {
        var user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserResponseDTO.fromUser(user));
    }
}

