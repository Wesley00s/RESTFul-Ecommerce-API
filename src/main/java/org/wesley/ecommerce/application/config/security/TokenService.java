package org.wesley.ecommerce.application.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
public class TokenService {

    @Value("${app.issuer}")
    private String issuer;

    @Value("${jwt.key.private.path}")
    private String privateKeyPath;

    @Value("${jwt.key.public.path}")
    private String publicKeyPath;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void loadKeys() {
        try {
            byte[] privateKeyBytes = loadKeyBytes(privateKeyPath);
            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpecPKCS8);

            byte[] publicKeyBytes = loadKeyBytes(publicKeyPath);
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicKeyBytes);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpecX509);

            log.info("Chaves RSA carregadas com sucesso.");

        } catch (Exception e) {
            log.error("Erro ao carregar as chaves RSA", e);
            throw new RuntimeException("Erro ao carregar as chaves RSA", e);
        }
    }

    private byte[] loadKeyBytes(String path) throws Exception {
        String content;
        if (path.startsWith("classpath:")) {
            InputStream is = getClass().getResourceAsStream("/" + path.substring(10));
            content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } else {
            content = new String(Files.readAllBytes(Paths.get(path)));
        }

        String key = content
                .replaceAll("\\n", "")
                .replaceAll("-----(BEGIN|END) (PRIVATE|PUBLIC) KEY-----", "");

        return Base64.getDecoder().decode(key);
    }

    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            Instant now = Instant.now();

            long expirationSeconds = 3600L * 24;
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
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);

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
