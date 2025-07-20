package org.wesley.ecommerce.application.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.UUID;

@Service
public interface UserService {
    Users findById(UUID id);

    Page<Users> findAll(Integer page, Integer pageSize);

    Users create(Users users);

    Users update(UUID id, UserDTO userDTO);

    void delete(Users users);

    Users findByEmail(String email);
}
