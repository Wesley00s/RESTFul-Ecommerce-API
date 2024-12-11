package org.wesley.ecommerce.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wesley.ecommerce.application.config.security.TokenService;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.UserService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TokenControllerTest {

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword(encoder.encode("password"));
        user.setUserType(UserType.CUSTOMER);

        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(tokenService.generateToken("test@example.com")).thenReturn("mocked-token");
    }


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private CartService cartService;


    @Test
    void testAuthSuccessful() throws Exception {
        String requestBody = """
            {
                "email": "test@example.com",
                "password": "password"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.token").value("mocked-token"))
                .andExpect(jsonPath("$.userType").value("CUSTOMER"));
    }


    @Test
    void testAuthInvalidCredentials() throws Exception {
        String requestBody = """
                {
                    "email": "test@example.com",
                    "password": "wrong-password"
                }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterSuccessful() throws Exception {
        String requestBody = """
                    {
                        "name": "New User",
                        "email": "newuser@example.com",
                        "password": "securePassword",
                        "userType": "CUSTOMER",
                        "street": "123 Main St",
                        "city": "Anywhere",
                        "state": "State",
                        "zip": "12345",
                        "createdAt": "2023-01-01T12:00:00"
                    }
                """;

        Users createdUser = new Users();
        createdUser.setId(UUID.randomUUID());
        createdUser.setName("New User");
        createdUser.setEmail("newuser@example.com");
        createdUser.setPassword(new BCryptPasswordEncoder().encode("securePassword"));
        createdUser.setUserType(UserType.CUSTOMER);

        when(userService.create(any())).thenReturn(createdUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Users successfully created!"));
    }

    @Test
    void testRegisterValidationFailure() throws Exception {
        String requestBody = """
                    {
                        "name": "",
                        "email": "invalid-email",
                        "password": "short",
                        "userType": "UNKNOWN",
                        "street": "",
                        "city": "",
                        "state": "",
                        "zip": "",
                        "createdAt": "invalid-date"
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
