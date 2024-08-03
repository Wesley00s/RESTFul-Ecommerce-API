package org.wesley.ecommerce.application.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * This class is responsible for configuring the security settings of the application.
 * It includes the configuration of JWT (JSON Web Tokens) for authentication and authorization.
 * It also sets up the HTTP security rules, disables CSRF (Cross-Site Request Forgery) protection,
 * and configures the session management policy.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * The public key used for verifying JWT tokens.
     * It is loaded from the application properties file using the @Value annotation.
     */
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    /**
     * The private key used for signing JWT tokens.
     * It is loaded from the application properties file using the @Value annotation.
     */
    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    /**
     * This method creates a JwtDecoder bean that uses the public key to decode JWT tokens.
     *
     * @return the configured JwtDecoder bean
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    /**
     * This method creates a JwtEncoder bean that uses the private key to sign JWT tokens.
     *
     * @return the configured JwtEncoder bean
     */
    @Bean
    protected JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * This method configures the security filter chain for the application.
     * It sets up the HTTP security rules, disables CSRF protection,
     * and configures the session management policy.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain bean
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                        )
                )
                .formLogin(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * This method creates a BCryptPasswordEncoder bean for encoding passwords.
     *
     * @return the configured BCryptPasswordEncoder bean
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
