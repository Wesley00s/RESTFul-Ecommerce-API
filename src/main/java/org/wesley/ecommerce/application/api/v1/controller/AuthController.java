package org.wesley.ecommerce.application.api.v1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.LoginRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.request.UserRequest;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.LoginResponse;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.MessageResponse;
import org.wesley.ecommerce.application.api.v1.controller.dto.response.UserResponse;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.UserService;

import java.time.Duration;

@RestController()
@RequestMapping("/v1/auth")
@Tag(name = "Auth Controller", description = "Managing token via OAuth configurations")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user and save storage token in cookies."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> auth(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var result = userService.authenticate(loginRequest.email(), loginRequest.password(), response);
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user and returns a confirmation message."
    )
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<MessageResponse> register(
            @RequestBody @Valid UserRequest user
    ) {
        var userCreated = userService.create(user);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(new MessageResponse("User successfully created!"));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, ResponseCookie.from("token", "")
                .maxAge(Duration.ZERO)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build()
                .toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthenticatedUser(Authentication authentication) {
        var user = (Users) authentication.getPrincipal();
        return ResponseEntity.ok(UserResponse.fromUser(user));
    }
  
}
