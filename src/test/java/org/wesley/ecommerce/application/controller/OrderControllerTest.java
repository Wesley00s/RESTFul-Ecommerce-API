package org.wesley.ecommerce.application.controller;

import jakarta.persistence.EntityNotFoundException;
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
import org.wesley.ecommerce.application.domain.enumeration.OrderStatus;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.*;
import org.wesley.ecommerce.application.exceptions.BusinessException;
import org.wesley.ecommerce.application.service.CartService;
import org.wesley.ecommerce.application.service.OrderService;
import org.wesley.ecommerce.application.service.ProductService;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
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
    void createOrder_CartNotFound_NotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(cartService.findCartByUserId(userId))
                .thenThrow(new EntityNotFoundException("Cart not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_CartEmpty_BadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(orderService.createOrderFromCart(userId))
                .thenThrow(new BusinessException("Cart is empty"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_ProductNotFound_NotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(orderService.createOrderFromCart(userId))
                .thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = ROLE_CUSTOMER)
    void createOrder_InsufficientStock_BadRequest() throws Exception {
        UUID userId = UUID.randomUUID();
        Users user = new Users(userId, "Test User", EMAIL_TEST_USER, "password", UserType.CUSTOMER, null, null, LocalDateTime.now());

        Mockito.when(userService.findByEmail(EMAIL_TEST_USER)).thenReturn(user);
        Mockito.when(orderService.createOrderFromCart(userId))
                .thenThrow(new BusinessException("Insufficient stock"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderNotFound_NotFound() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderService.confirmOrder(orderId, true))
                .thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderItemsEmpty_BadRequest() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderService.confirmOrder(orderId, true))
                .thenThrow(new BusinessException("Order has no items"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_InsufficientStock_BadRequest() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderService.confirmOrder(orderId, true))
                .thenThrow(new BusinessException("Insufficient stock"));

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderConfirmedSuccessfully() throws Exception {
        Long orderId = 1L;
        OrderShopping order = new OrderShopping();
        order.setStatus(OrderStatus.COMPLETED);

        Mockito.when(orderService.confirmOrder(orderId, true)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = EMAIL_TEST_USER, roles = "ADMIN")
    void confirmOrder_OrderCancelledSuccessfully() throws Exception {
        Long orderId = 1L;
        OrderShopping order = new OrderShopping();
        order.setStatus(OrderStatus.CANCELLED);

        Mockito.when(orderService.confirmOrder(orderId, false)).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/order/admin/confirm")
                        .param("order", orderId.toString())
                        .param("confirm", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}