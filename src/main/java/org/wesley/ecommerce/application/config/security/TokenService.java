package org.wesley.ecommerce.application.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.private.key}")
    private String privateKey;

    public String generateToken(String email) {
        try {

            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            var now = Instant.now();
            var expireIn = 3600L;

            return JWT.create()
                    .withIssuer("https://github.com/Wesley00s/RESTFul-Ecommerce-API")
                    .withSubject(email)
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(now.plusSeconds(expireIn)))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error creating token");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            return JWT.require(algorithm)
                    .withIssuer("https://github.com/Wesley00s/RESTFul-Ecommerce-API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException e) {
            return null;
        }
    }



}
