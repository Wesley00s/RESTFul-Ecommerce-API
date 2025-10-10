package org.wesley.ecommerce.application.api.v1.controller.dto.response;

public record ApiInfoResponse(
        String title,
        String description,
        String version,
        String termsOfService,
        String contactName,
        String contactEmail,
        String contactUrl
) {
}
