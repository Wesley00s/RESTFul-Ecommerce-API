package org.wesley.ecommerce.application.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wesley.ecommerce.application.config.security.TokenService;
import org.wesley.ecommerce.application.controller.dto.LoginRequest;
import org.wesley.ecommerce.application.controller.dto.LoginResponse;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
class TokenControllerTest {
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CartService cartService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void testAuthSuccess() throws Exception {
        var loginRequest = new LoginRequest("user@example.com", "password");
        var user = new Users();
        user.setEmail("user@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setName("John Doe");
        user.setUserType(UserType.CUSTOMER);

        when(userService.findByEmail(eq("user@example.com"))).thenReturn(user);
        when(tokenService.generateToken(eq("user@example.com"))).thenReturn("mock-token");

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"John Doe\", \"token\":\"mock-token\", \"userType\":\"CUSTOMER\"}"));
    }

    @Test
    void testAuthFailure() throws Exception {
        var loginRequest = new LoginRequest("user@example.com", "wrongpassword");
        var user = new Users();
        user.setEmail("user@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));

        when(userService.findByEmail(eq("user@example.com"))).thenReturn(user);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

}