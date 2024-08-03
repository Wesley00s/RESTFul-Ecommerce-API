package org.wesley.ecommerce.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wesley.ecommerce.application.controller.dto.LoginRequest;
import org.wesley.ecommerce.application.controller.dto.LoginResponse;
import org.wesley.ecommerce.application.service.UserService;

import java.time.Instant;

/**
 * This class is responsible for managing token generation and validation for OAuth configurations.
 * It provides a RESTful endpoint for user login using OAuth.
 *
 * @author Wesley
 * @since 1.0
 */
@RestController("/ecommerce")
@Tag(name = "Token Controller", description = "Managing token via OAuth configurations")
public class TokenController {
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructor for TokenController.
     *
     * @param jwtEncoder            The JWT encoder for generating and validating JWT tokens.
     * @param userService           The service for managing user data.
     * @param bCryptPasswordEncoder The encoder for hashing and comparing passwords.
     */
    public TokenController(JwtEncoder jwtEncoder, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Endpoint for user login using OAuth.
     *
     * @param loginRequest The request containing the user's email and password.
     * @return A ResponseEntity containing the JWT token and its expiration time if the login is successful.
     *         Throws a BadCredentialsException if the email or password is invalid.
     */
    @PostMapping("/login-oauth")
    @Operation(summary = "Log in via OAuth", description = "Allows a user to log in using OAuth.")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userService.findByEmail(loginRequest.email());
        if (user == null || !userService.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Invalid email or password");
        }

        var now = Instant.now();
        var expireIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("https://github.com/Wesley00s/RESTFul-Ecommerce-API")
                .subject(loginRequest.email())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expireIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new LoginResponse(jwtValue, expireIn));
    }
}
