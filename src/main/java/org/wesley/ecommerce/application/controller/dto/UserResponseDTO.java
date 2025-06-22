package org.wesley.ecommerce.application.controller.dto;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Users;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String password,
        UserType userType,
        String street,
        String city,
        String state,
        String zip,
        LocalDateTime createdAt
) {

    public static UserResponseDTO fromUser(Users users) {
        return new UserResponseDTO(
                users.getId(),
                users.getName(),
                users.getEmail(),
                users.getPassword(),
                users.getUserType(),
                users.getAddress().getStreet(),
                users.getAddress().getCity(),
                users.getAddress().getState(),
                users.getAddress().getZip(),
                users.getCreatedAt()
        );
    }
}
