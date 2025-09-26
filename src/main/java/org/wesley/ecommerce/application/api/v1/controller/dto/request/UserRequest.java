package org.wesley.ecommerce.application.api.v1.controller.dto.request;

import jakarta.validation.constraints.*;
import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Address;
import org.wesley.ecommerce.application.domain.model.Users;

import java.time.LocalDateTime;

public record UserRequest(
        @NotBlank(message = "The name is required.")
        @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters.")
        String name,

        @NotBlank(message = "The email is required.")
        @Email(message = "The email format is invalid.")
        String email,

        @NotBlank(message = "The password is required.")
        @Size(min = 6, message = "The password must be at least 6 characters long.")
        String password,

        @NotNull(message = "The user type is required.")
        UserType userType,

        @NotBlank(message = "The street is required.")
        String street,

        @NotBlank(message = "The city is required.")
        String city,

        @NotBlank(message = "The state is required.")
        @Size(min = 2, max = 2, message = "The state must be a 2-character abbreviation (e.g., NY).")
        String state,

        @NotBlank(message = "The zip code is required.")
        @Pattern(regexp = "^\\d{8}$", message = "The zip code must contain exactly 8 numeric digits.")
        String zip
) {

    public Users from() {
        var address = new Address();
        address.setStreet(street());
        address.setCity(city());
        address.setState(state());
        address.setZip(zip());

        var usr = new Users();
        usr.setName(name());
        usr.setEmail(email());
        usr.setPassword(password());
        usr.setUserType(userType());
        usr.setAddress(address);
        usr.setCreatedAt(LocalDateTime.now());

        return usr;
    }
}
