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
        System.setProperty("POSTGRES_HOST", Objects.requireNonNull(dotenv.get("POSTGRES_HOST")));
        System.setProperty("POSTGRES_DB", Objects.requireNonNull(dotenv.get("POSTGRES_DB")));
        System.setProperty("POSTGRES_PORT", Objects.requireNonNull(dotenv.get("POSTGRES_PORT")));
        System.setProperty("POSTGRES_USER", Objects.requireNonNull(dotenv.get("POSTGRES_USER")));
        System.setProperty("POSTGRES_PASSWORD", Objects.requireNonNull(dotenv.get("POSTGRES_PASSWORD")));
        System.setProperty("PUBLIC_KEY", Objects.requireNonNull(dotenv.get("PUBLIC_KEY")));
        System.setProperty("PRIVATE_KEY", Objects.requireNonNull(dotenv.get("PRIVATE_KEY")));
        System.setProperty("ISSUER", Objects.requireNonNull(dotenv.get("ISSUER")));
    }
}
