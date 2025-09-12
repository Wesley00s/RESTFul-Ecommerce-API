package org.wesley.ecommerce.application.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.UserType;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String name,
        String email,
        UserType userType
) {

    public static LoginResponse of(AuthResponse authResponse) {
        return new LoginResponse(
                authResponse.id(),
                authResponse.name(),
                authResponse.email(),
                authResponse.userType()
        );
    }

}