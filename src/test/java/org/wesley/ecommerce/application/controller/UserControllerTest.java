package org.wesley.ecommerce.application.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wesley.ecommerce.application.controller.dto.UserDTO;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @Test
    void getUser_ValidId_ReturnsUserDTO() throws Exception {
        UUID userId = UUID.randomUUID();

        Users mockUser = new Users();
        mockUser.setName("John Doe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setPassword("password");
        mockUser.setUserType(UserType.CUSTOMER);
        mockUser.setCreatedAt(LocalDateTime.now());

        mockUser.setAddress(new Address(1L, "123 Street", "City", "State", "12345"));
        Mockito.when(userService.findById(eq(userId))).thenReturn(mockUser);

        mockMvc.perform(get("/user/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.userType").value(UserType.CUSTOMER.toString()))
                .andExpect(jsonPath("$.street").value("123 Street"))
                .andExpect(jsonPath("$.city").value("City"))
                .andExpect(jsonPath("$.state").value("State"))
                .andExpect(jsonPath("$.zip").value("12345"));
    }

    @Test
    void getUsers_ReturnsPaginatedUserDTOs() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        Users mockUser1 = new Users();
        mockUser1.setId(userId);
        mockUser1.setName("John Doe");
        mockUser1.setEmail("johndoe@example.com");
        mockUser1.setPassword("password");
        mockUser1.setUserType(UserType.CUSTOMER);
        mockUser1.setCreatedAt(LocalDateTime.now());
        mockUser1.setAddress(new Address(1L, "123 Street", "City", "State", "12345"));

        Users mockUser2 = new Users();
        mockUser2.setId(userId2);
        mockUser2.setName("Jane Smith");
        mockUser2.setEmail("janesmith@example.com");
        mockUser2.setPassword("password");
        mockUser2.setUserType(UserType.ADMIN);
        mockUser2.setCreatedAt(LocalDateTime.now());
        mockUser2.setAddress(new Address(2L, "456 Avenue", "Town", "Region", "67890"));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Users> userPage = new PageImpl<>(List.of(mockUser1, mockUser2), pageable, 2);

        Mockito.when(userService.findAll(0, 10)).thenReturn(userPage);

        mockMvc.perform(get("/user")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.pagination").exists())

                .andExpect(jsonPath("$.data[0].id").value(userId.toString()))
                .andExpect(jsonPath("$.data[0].name").value("John Doe"))
                .andExpect(jsonPath("$.data[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.data[0].userType").value(UserType.CUSTOMER.toString()))
                .andExpect(jsonPath("$.data[0].street").value("123 Street"))
                .andExpect(jsonPath("$.data[0].city").value("City"))
                .andExpect(jsonPath("$.data[0].state").value("State"))
                .andExpect(jsonPath("$.data[0].zip").value("12345"))

                .andExpect(jsonPath("$.data[1].id").value(userId2.toString()))
                .andExpect(jsonPath("$.data[1].name").value("Jane Smith"))
                .andExpect(jsonPath("$.data[1].email").value("janesmith@example.com"))
                .andExpect(jsonPath("$.data[1].userType").value(UserType.ADMIN.toString()))
                .andExpect(jsonPath("$.data[1].street").value("456 Avenue"))
                .andExpect(jsonPath("$.data[1].city").value("Town"))
                .andExpect(jsonPath("$.data[1].state").value("Region"))
                .andExpect(jsonPath("$.data[1].zip").value("67890"))

                .andExpect(jsonPath("$.pagination.page").value(0))
                .andExpect(jsonPath("$.pagination.size").value(10))
                .andExpect(jsonPath("$.pagination.totalElements").value(2))
                .andExpect(jsonPath("$.pagination.totalPages").value(1));
    }

    @Test
    void deleteUser_ValidId_UserDeleted() throws Exception {
        UUID userId = UUID.randomUUID();
        Users mockUser = new Users();

        Mockito.when(userService.findById(userId)).thenReturn(mockUser);
        Mockito.doNothing().when(userService).delete(mockUser);

        mockMvc.perform(delete("/user/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_InvalidId_UserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(delete("/user/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ValidId_ReturnsUpdatedUserDTO() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDTO userDTO = new UserDTO(
                "John Doe Updated",
                "johnupdated@example.com",
                "newpassword",
                UserType.ADMIN,
                "456 New Street",
                "New City",
                "New State",
                "67890",
                LocalDateTime.now()
        );

        Users updatedUser = new Users();
        updatedUser.setName(userDTO.name());
        updatedUser.setEmail(userDTO.email());
        updatedUser.setPassword(userDTO.password());
        updatedUser.setUserType(userDTO.userType());
        updatedUser.setAddress(new Address(1L, userDTO.street(), userDTO.city(), userDTO.state(), userDTO.zip()));

        Mockito.when(userService.findById(userId)).thenReturn(new Users());
        Mockito.when(userService.update(eq(userId), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe Updated\", \"email\": \"johnupdated@example.com\", \"password\": \"newpassword\", \"userType\": \"ADMIN\", \"street\": \"456 New Street\", \"city\": \"New City\", \"state\": \"New State\", \"zip\": \"67890\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.email").value("johnupdated@example.com"))
                .andExpect(jsonPath("$.userType").value(UserType.ADMIN.toString()))
                .andExpect(jsonPath("$.street").value("456 New Street"))
                .andExpect(jsonPath("$.city").value("New City"))
                .andExpect(jsonPath("$.state").value("New State"))
                .andExpect(jsonPath("$.zip").value("67890"));
    }

    @Test
    void updateUser_InvalidId_ReturnsNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        Mockito.when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(put("/user/{id}", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John Doe Updated\", \"email\": \"johnupdated@example.com\", \"password\": \"newpassword\", \"userType\": \"ADMIN\", \"street\": \"456 New Street\", \"city\": \"New City\", \"state\": \"New State\", \"zip\": \"67890\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findUserByEmail_ValidEmail_ReturnsUserDTO() throws Exception {
        String email = "johndoe@example.com";

        Users mockUser = new Users();
        mockUser.setName("John Doe");
        mockUser.setEmail(email);
        mockUser.setPassword("password");
        mockUser.setUserType(UserType.CUSTOMER);
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setAddress(new Address(1L, "123 Street", "City", "State", "12345"));

        Mockito.when(userService.findByEmail(eq(email))).thenReturn(mockUser);

        mockMvc.perform(get("/user/mail/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.userType").value(UserType.CUSTOMER.toString()))
                .andExpect(jsonPath("$.street").value("123 Street"))
                .andExpect(jsonPath("$.city").value("City"))
                .andExpect(jsonPath("$.state").value("State"))
                .andExpect(jsonPath("$.zip").value("12345"));
    }

    @Test
    void findUserByEmail_InvalidEmail_ReturnsNotFound() throws Exception {
        String email = "invalid@example.com";

        Mockito.when(userService.findByEmail(email)).thenReturn(null);

        mockMvc.perform(get("/user/mail/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}