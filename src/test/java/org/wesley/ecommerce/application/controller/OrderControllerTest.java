package org.wesley.ecommerce.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.*;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
class OrderControllerTest {

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    private static final String EMAIL_TEST_USER = "test@example.com";
    private static final String ROLE_CUSTOMER = "CUSTOMER";

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_CartNotFound_ForbidAccess() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER))
                .thenReturn(new Users(UUID.randomUUID(), "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now()));
        Mockito.when(cartService.findCartByUserId(UUID.randomUUID())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("You do not have access to this cart."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_CartEmpty_BadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());
        Cart cart = new Cart();
        cart.setUsers(user);
        cart.setItems(Collections.emptyList());
        cart.setTotalPrice(0.0);

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(cartService.findCartByUserId(userId)).thenReturn(cart);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cart is empty. Cannot create an order."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_ProductNotFound_NotFoundStatus() throws Exception {
        Product product = new Product();
        product.setId(1L);
        CartItem item = new CartItem();
        item.setId(2L);
        item.setProduct(product);
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());
        Cart cart = new Cart();
        cart.setUsers(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotalPrice(100.0);

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(cartService.findCartByUserId(userId)).thenReturn(cart);
        Mockito.when(productService.findById(product.getId())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found: " + product.getId()));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_InsufficientStock_BadRequest() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setStock(5);

        CartItem item = new CartItem();
        item.setId(2L);
        item.setProduct(product);
        item.setQuantity(10);

        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());

        Cart cart = new Cart();
        cart.setUsers(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotalPrice(100.0);

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(cartService.findCartByUserId(userId)).thenReturn(cart);
        Mockito.when(productService.findById(product.getId())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Insufficient stock for product: " + product.getName()));

        Mockito.verify(userService).findByEmail(EMAIL_TEST_USER);
        Mockito.verify(cartService).findCartByUserId(userId);
        Mockito.verify(productService).findById(product.getId());
    }


    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_OrderCreationSuccess() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        CartItem item = new CartItem();
        item.setId(2L);
        item.setProduct(product);
        item.setQuantity(5);
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());
        Cart cart = new Cart();
        cart.setUsers(user);
        cart.setItems(Collections.singletonList(item));
        cart.setTotalPrice(100.0);

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(cartService.findCartByUserId(userId)).thenReturn(cart);
        Mockito.when(productService.findById(product.getId())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order created successfully and sent for admin confirmation."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderNotFound_NotFoundStatus() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderService.findById(orderId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Order not found."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_CartEmpty_BadRequest() throws Exception {
        Long orderId = 1L;
        OrderShopping order = new OrderShopping();
        order.setCart(new Cart());

        Mockito.when(orderService.findById(orderId)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Cart is empty. Cannot confirm an order."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_InsufficientStock_BadRequest() throws Exception {
        Long orderId = 1L;
        Product product = new Product();
        product.setId(1L);
        product.setStock(2);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        OrderShopping order = new OrderShopping();
        order.setCart(cart);

        Mockito.when(orderService.findById(orderId)).thenReturn(order);
        Mockito.when(productService.findById(product.getId())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Insufficient stock for product: " + product.getName()));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_ProductNotFound_NotFoundStatus() throws Exception {
        Long orderId = 1L;
        Product product = new Product();
        product.setId(1L);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        OrderShopping order = new OrderShopping();
        order.setCart(cart);

        Mockito.when(orderService.findById(orderId)).thenReturn(order);
        Mockito.when(productService.findById(product.getId())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Product not found: " + product.getId()));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderConfirmedSuccessfully() throws Exception {
        Long orderId = 1L;
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);
        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        OrderShopping order = new OrderShopping();
        order.setCart(cart);

        Mockito.when(orderService.findById(orderId)).thenReturn(order);
        Mockito.when(productService.findById(product.getId())).thenReturn(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order confirmed successfully."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderCancelledSuccessfully() throws Exception {
        Long orderId = 1L;
        OrderShopping order = new OrderShopping();
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(new CartItem()));
        order.setCart(cart);

        Mockito.when(orderService.findById(orderId)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order cancelled successfully."));
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderCancellationSuccess() throws Exception {
        Long orderId = 1L;
        OrderShopping order = new OrderShopping();
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(new CartItem()));
        order.setCart(cart);

        Mockito.when(orderService.findById(orderId)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order cancelled successfully."));
    }
}