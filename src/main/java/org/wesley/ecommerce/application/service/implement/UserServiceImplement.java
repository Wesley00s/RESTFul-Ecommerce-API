package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.controller.dto.UserDTO;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.UserRepository;
import org.wesley.ecommerce.application.service.UserService;

import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class UserServiceImplement implements UserService {

    final private UserRepository userRepository;

    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Users not found with id " + id));
    }

    @Override
    public Page<Users> findAll(Integer page, Integer pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize));
    }

    @Override
    public Users create(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users update(UUID id, UserDTO userDTO) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (userDTO.name() != null) existingUser.setName(userDTO.name());
        if (userDTO.email() != null) existingUser.setEmail(userDTO.email());
        if (userDTO.password() != null) existingUser.setPassword(userDTO.password());
        if (userDTO.userType() != null) existingUser.setUserType(userDTO.userType());
        if (userDTO.street() != null) existingUser.getAddress().setStreet(userDTO.street());
        if (userDTO.city() != null) existingUser.getAddress().setCity(userDTO.city());
        if (userDTO.state() != null) existingUser.getAddress().setState(userDTO.state());
        if (userDTO.zip() != null) existingUser.getAddress().setZip(userDTO.zip());

        return userRepository.save(existingUser);
    }

    @Override
    public void delete(Users users) {
        if (userRepository.existsById(users.getId())) {
            userRepository.deleteById(users.getId());
        } else {
            throw new NoSuchElementException("Users with id " + users.getId() + " not found for delete");
        }
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Users not found with email: " + email));
    }
}