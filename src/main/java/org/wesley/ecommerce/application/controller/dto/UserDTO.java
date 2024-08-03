package org.wesley.ecommerce.application.controller.dto;

import jakarta.validation.constraints.Email;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.User;

/**
 * A data transfer object (DTO) representing a user in the e-commerce application.
 * This class encapsulates user information and provides methods to convert to and from a {@link User} domain object.
 *
 * @param name       the user's name
 * @param email      the user's email address
 * @param password   the user's password
 * @param userType   the type of user (e.g., customer, admin)
 * @param street     the user's street address
 * @param city       the user's city
 * @param state      the user's state
 * @param zip        the user's zip code
 */
public record UserDTO(
        String name,
        String email,
        String password,
        UserType userType,
        String street,
        String city,
        String state,
        String zip
) {

    /**
     * Creates a new {@link UserDTO} instance from a given {@link User} domain object.
     *
     * @param user the user domain object
     * @return a new {@link UserDTO} instance
     */
    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserType(),
                user.getAddress().getStreet(),
                user.getAddress().getCity(),
                user.getAddress().getState(),
                user.getAddress().getZip()
        );
    }

    /**
     * Creates a new {@link User} domain object from the current {@link UserDTO} instance.
     *
     * @return a new {@link User} domain object
     */
    public User from() {
        var address = new Address();
        address.setStreet(street());
        address.setCity(city());
        address.setState(state());
        address.setZip(zip());

        var usr = new User();
        usr.setName(name());
        usr.setEmail(email());
        usr.setPassword(password());
        usr.setUserType(userType());
        usr.setAddress(address);
        return usr;
    }
}
