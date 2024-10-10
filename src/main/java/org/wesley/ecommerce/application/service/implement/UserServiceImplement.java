package org.wesley.ecommerce.application.service.implement;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wesley.ecommerce.application.domain.model.User;
import org.wesley.ecommerce.application.domain.repository.UserRepository;
import org.wesley.ecommerce.application.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * This class implements the UserService interface and provides methods for managing user data.
 * It uses Spring Data JPA to interact with the database.
 */
@Service
public class UserServiceImplement implements UserService {
    /**
     * The UserRepository interface for database operations.
     */
    final private UserRepository userRepository;

    /**
     * Constructor for UserServiceImplement.
     *
     * @param userRepository The UserRepository interface for database operations.
     */
    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by their unique identifier (UUID).
     *
     * @param id The UUID of the user to find.
     * @return The user with the given UUID.
     * @throws NoSuchElementException If no user is found with the given UUID.
     */
    @Override
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The user to create.
     * @return The created user.
     */
    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param id          The UUID of the user to update.
     * @param updatedUser The updated user data.
     * @return The updated user.
     * @throws NoSuchElementException If no user is found with the given UUID.
     */
    @Override
    public User update(UUID id, User updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setName(updatedUser.getName());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setUserType(updatedUser.getUserType());
            return userRepository.save(existingUser);
        } else {
            throw new NoSuchElementException("User not found for update");
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The user to delete.
     * @throws NoSuchElementException If no user is found with the given UUID.
     */
    @Override
    public void delete(User user) {
        if (userRepository.existsById(user.getUserId())) {
            userRepository.deleteById(user.getUserId());
        } else {
            throw new NoSuchElementException("User with id " + user.getUserId() + " not found for delete");
        }
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return The user with the given email address.
     * @throws NoSuchElementException If no user is found with the given email address.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}