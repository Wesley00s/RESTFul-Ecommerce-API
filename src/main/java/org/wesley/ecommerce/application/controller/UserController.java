package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing users.
 *
 * @author Wesley
 */
@RestController
@RequestMapping("/user")
@Tag(name = "Users Controller", description = "RESTFul API for managing users.")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    /**
     * Retrieve a specific user based on its ID.
     *
     * @param id ID of the user to be retrieved.
     * @return ResponseEntity containing the retrieved user's data.
     */
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

    /**
     * Retrieve a list of all registered users.
     *
     * @return ResponseEntity containing a list of all registered users.
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Remove user", description = "Remove user by ID")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        var userToDelete = userService.findById(id);
        userService.delete(userToDelete);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the data of an existing user based on its ID.
     *
     * @param id       ID of the user to be updated.
     * @param userDTO Updated user data.
     * @return ResponseEntity containing the updated user's data.
     */
    @Operation(summary = "Update a user", description = "Update the data of an existing user based on its ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        var updatedUser = userService.update(id, userDTO.from());
        return ResponseEntity.ok(UserDTO.fromUser(updatedUser));
    }

    @Operation(summary = "Find user by email", description = "Retrieve a specific user based on its email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> findUserByEmail(@PathVariable String email) {
        var user = userService.findByEmail(email);
        return ResponseEntity.ok(UserDTO.fromUser(user));
    }

}

