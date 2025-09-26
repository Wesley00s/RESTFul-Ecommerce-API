package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import org.wesley.ecommerce.application.domain.enumeration.UserType;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID id,
        String name,
        String email,
        UserType userType
) {
}