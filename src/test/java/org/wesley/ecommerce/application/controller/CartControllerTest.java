package org.wesley.ecommerce.application.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wesley.ecommerce.application.domain.model.Cart;
import org.wesley.ecommerce.application.domain.model.Product;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.CartItemService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.implement.AuthenticationService;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartControllerTest {
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String EMAIL_TEST_USER = "test@example.com";
    private static final String ROLE_CUSTOMER = "CUSTOMER";

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    CartItemService cartService;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    public void addProductFromCart_ProductNotFound() throws Exception {
        Users user = new Users();
        Cart cart = new Cart();
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(authenticationService.getActiveCart(user)).thenReturn(cart);
        when(productService.findById(anyLong())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                        .param("product", "1")
                        .param("quantity", "1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    public void addProductFromCart_InsufficientStock() throws Exception {
        Users user = new Users();
        Cart cart = new Cart();
        Product product = new Product();
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(authenticationService.getActiveCart(user)).thenReturn(cart);
        when(productService.findById(anyLong())).thenReturn(product);
        when(productService.isStockAvailable(anyLong(), any(Integer.class))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                        .param("product", "1")
                        .param("quantity", "5"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Product is not available"));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    public void removeAllProductsReferencesFromCart_UserHasNoActiveCart() throws Exception {
        Users user = new Users();
        when(authenticationService.getAuthenticatedUser()).thenReturn(user);
        when(authenticationService.getActiveCart(user)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart")
                        .param("product", "1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("User does not have an active cart."));
    }
}