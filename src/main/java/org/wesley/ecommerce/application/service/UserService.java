package org.wesley.ecommerce.application.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.controller.dto.response.AuthResponse;
import org.wesley.ecommerce.application.controller.dto.request.UserRequest;
import org.wesley.ecommerce.application.controller.dto.response.LoginResponse;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.UUID;

@Service
public interface UserService {
    Users findById(UUID id);

    Page<Users> findAll(Integer page, Integer pageSize);

    Users create(UserRequest users);

    Users update(UUID id, UserRequest userRequest);

    void delete(Users users);

    Users findByEmail(String email);

    LoginResponse authenticate(String email, String password, HttpServletResponse response);
}
