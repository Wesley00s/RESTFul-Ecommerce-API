package org.wesley.ecommerce.application.config.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                .servers(
                    List.of(
                        new Server().url("http://localhost:8080/ecommerce").description("Development Server"),
                        new Server().url("https://api.ecommerce.com").description("Production Server")
                    )
                )

                .info(new Info()
                        .title("Ecommerce API")
                        .version("1.0")
                        .description("Ecommerce System API Documentation.")
                        .contact(new Contact()
                                .email("wesley300rodrigues@gmail.com")
                                .name("Wesley Rodrigues")
                                .url("https://github.com/Wesley00s/")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}