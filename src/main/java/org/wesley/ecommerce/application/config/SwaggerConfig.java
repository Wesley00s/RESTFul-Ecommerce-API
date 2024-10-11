package org.wesley.ecommerce.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;


@Configuration
@CrossOrigin(origins = {"/**"})
public class SwaggerConfig {

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
