package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.Users;
import org.wesley.ecommerce.application.domain.repository.UserRepository;
import org.wesley.ecommerce.application.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users create(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users update(UUID id, Users updatedUsers) {
        Optional<Users> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            Users existingUsers = existingUserOptional.get();
            existingUsers.setEmail(updatedUsers.getEmail());
            existingUsers.setName(updatedUsers.getName());
            existingUsers.setPassword(updatedUsers.getPassword());
            existingUsers.setUserType(updatedUsers.getUserType());
            return userRepository.save(existingUsers);
        } else {
            throw new NoSuchElementException("Users not found for update");
        }
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