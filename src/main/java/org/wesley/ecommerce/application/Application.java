package org.wesley.ecommerce.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.wesley.ecommerce.application.config.DotenvConfig;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        DotenvConfig.loadDotenv();
        SpringApplication.run(Application.class, args);
    }

}
