package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import java.util.List;

public record ApiResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
}