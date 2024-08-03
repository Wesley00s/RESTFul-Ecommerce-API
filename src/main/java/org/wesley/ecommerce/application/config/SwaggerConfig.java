package org.wesley.ecommerce.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * This class is responsible for configuring Swagger documentation for the Ecommerce API.
 * It uses the OpenAPI library to define the API's information, contact details, and other settings.
 *
 * @author Wesley
 * @version 1.0
 * @since 2024-03-08
 */
@Configuration
@CrossOrigin(origins = {"/**"})
public class SwaggerConfig {

    /**
     * This method creates and returns an instance of OpenAPI, which is used to configure the API's documentation.
     *
     * @return An instance of OpenAPI with the specified API information, contact details, and other settings.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Ecommerce API")
                .version("1.0")
                .description("Ecommerce API Documentation")
                .summary("Ecommerce API Documentation")
                .contact(new Contact()
                        .email("wesley300rodrigues@gmail.com")
                        .name("Wesley")
                        .url("https://github.com/Wesley00s/")
                ));
    }
}
