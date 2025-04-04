package org.wesley.ecommerce.application.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public static void loadDotenv() {
        Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("DATABASE_HOST", Objects.requireNonNull(dotenv.get("DATABASE_HOST")));
        System.setProperty("DATABASE_NAME", Objects.requireNonNull(dotenv.get("DATABASE_NAME")));
        System.setProperty("DATABASE_PORT", Objects.requireNonNull(dotenv.get("DATABASE_PORT")));
        System.setProperty("DATABASE_USER", Objects.requireNonNull(dotenv.get("DATABASE_USER")));
        System.setProperty("DATABASE_PASSWORD", Objects.requireNonNull(dotenv.get("DATABASE_PASSWORD")));
        System.setProperty("PUBLIC_KEY", Objects.requireNonNull(dotenv.get("PUBLIC_KEY")));
        System.setProperty("PRIVATE_KEY", Objects.requireNonNull(dotenv.get("PRIVATE_KEY")));
        System.setProperty("ISSUER", Objects.requireNonNull(dotenv.get("ISSUER")));
    }
}
