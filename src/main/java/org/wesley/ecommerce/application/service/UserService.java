package org.wesley.ecommerce.application.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.controller.dto.LoginRequest;
import org.wesley.ecommerce.application.domain.model.User;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    User findById(UUID id);
    List<User> findAll();
    User create(User user);
    User update(UUID id, User user);
    void delete(User user);
    User findByEmail(String email);
}
