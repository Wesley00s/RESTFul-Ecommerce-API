package org.wesley.ecommerce.application.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {

}