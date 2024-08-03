package org.wesley.ecommerce.application.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides configuration for the server used by the Spring Boot application.
 */
@Configuration
public class ServerConfig {

    /**
     * Customizes the {@link ConfigurableServletWebServerFactory} to set port, context path,
     * and session timeout.
     *
     * @return a {@link WebServerFactoryCustomizer} that applies the customizations
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            if (factory instanceof TomcatServletWebServerFactory) {
                ((TomcatServletWebServerFactory) factory).addContextCustomizers(context -> {
                    context.setSessionTimeout(30 * 60);
                });
            }
            factory.setPort(8080);
            factory.setContextPath("/ecommerce");
        };
    }
}
