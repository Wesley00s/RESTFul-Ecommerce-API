package org.wesley.ecommerce.application.api.v1.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
    String parentCommentId,

    @NotBlank @Size(max = 1000)
    String content,

    String mentionedUserId,
    String mentionedUserName
) {}