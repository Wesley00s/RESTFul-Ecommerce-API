package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.config.security.TokenService;
import org.wesley.ecommerce.application.controller.dto.LoginRequest;
import org.wesley.ecommerce.application.controller.dto.LoginResponse;
import org.wesley.ecommerce.application.controller.dto.RegisterResponse;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

@RestController("/ecommerce")
@Tag(name = "Token Controller", description = "Managing token via OAuth configurations")
@RequiredArgsConstructor
public class TokenController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final CartService cartService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token.")
    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> auth(@RequestBody LoginRequest loginRequest) {
        var user = userService.findByEmail(loginRequest.email());

        if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {

            String token = tokenService.generateToken(user.getEmail());

            return ResponseEntity.ok(new LoginResponse(user.getName(), token, user.getUserType().toString()));
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Register a new user", description = "Registers a new user and returns a confirmation message.")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<RegisterResponse> register(@RequestBody UserDTO user) {
        var userToCreate = user.from();
        userToCreate.setPassword(bCryptPasswordEncoder.encode(user.password()));
        var userCreated = userService.create(userToCreate);
        var cart = new Cart();
        cart.setUsers(userCreated);
        cartService.create(cart);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(new RegisterResponse("Users successfully created!"));
    }
}
