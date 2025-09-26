package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.UserType;
import org.wesley.ecommerce.application.domain.model.Users;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserType userType,
        String street,
        String city,
        String state,
        String zip,
        LocalDateTime createdAt
) {

    public static UserResponse fromUser(Users users) {
        return new UserResponse(
                users.getId(),
                users.getName(),
                users.getEmail(),
                users.getUserType(),
                users.getAddress().getStreet(),
                users.getAddress().getCity(),
                users.getAddress().getState(),
                users.getAddress().getZip(),
                users.getCreatedAt()
        );
    }
}
