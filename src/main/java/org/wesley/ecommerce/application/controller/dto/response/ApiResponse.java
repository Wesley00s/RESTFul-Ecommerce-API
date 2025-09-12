package org.wesley.ecommerce.application.controller.dto.response;

import java.util.List;

public record ApiResponse<T>(
        List<T> data,
        PaginationResponse pagination
) {
}