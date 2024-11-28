package org.wesley.ecommerce.application.service;

import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Users;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    Users findById(UUID id);

    List<Users> findAll();

    Users create(Users users);

    Users update(UUID id, Users users);

    void delete(Users users);

    Users findByEmail(String email);
}
