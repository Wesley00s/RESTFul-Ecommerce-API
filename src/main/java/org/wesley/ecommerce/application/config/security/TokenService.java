package org.wesley.ecommerce.application.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${jwt.private.key}")
    private String privateKey;

    @Value("${jwt.issuer}")
    private String issuer;


    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            Instant now = Instant.now();

            long expirationSeconds = 3600L;
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(email)
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Erro ao criar o token JWT para o email {} : {}", email, e.getMessage());
            throw new RuntimeException("Erro ao criar o token JWT");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Token JWT inválido ou expirado: {}", e.getMessage());
            return null;
        }
    }
}
