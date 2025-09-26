package org.wesley.ecommerce.application.api.v1.controller.dto.event;

import java.util.UUID;

public record CommentAddedEvent(
    UUID reviewId,
    String commentId,
    String parentCommentId,
    UUID customerId,
    String customerName,
    String content,
    String mentionedUserId,
    String mentionedUserName
) {}