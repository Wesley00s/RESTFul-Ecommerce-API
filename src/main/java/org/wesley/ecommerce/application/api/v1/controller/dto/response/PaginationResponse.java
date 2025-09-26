package org.wesley.ecommerce.application.api.v1.controller.dto.response;

import org.springframework.data.domain.Page;

public record PaginationResponse(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages
) {
    public static PaginationResponse from(
            Page<?> page
    ) {
        return new PaginationResponse(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}