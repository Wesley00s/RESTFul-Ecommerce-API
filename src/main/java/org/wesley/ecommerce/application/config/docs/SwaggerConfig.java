package org.wesley.ecommerce.application.config.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "cookieAuth";
        final String cookieName = "token";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(cookieName)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080/ecommerce/api").description("Development Server"),
                                new Server().url("https://api.ecommerce.com").description("Production Server")
                        )
                )
                .info(new Info()
                        .title("Ecommerce API") 
                        .description("Documentação geral da API do sistema de E-commerce.")
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

    @Bean
    public GroupedOpenApi publicApiV1() {
        return GroupedOpenApi.builder()
                .group("v1") 
                .pathsToMatch("/v1/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.setInfo(new Info()
                            .title("Ecommerce API - V1")
                            .version("1.0")
                            .description("API versão 1.0, focada nos recursos iniciais do e-commerce. Esta versão é estável."));
                })
                .build();
    }

}