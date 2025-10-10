package org.wesley.ecommerce.application.exceptions.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.wesley.ecommerce.application.exceptions.model.ExceptionDetails;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ExceptionsUtils {
    protected ResponseEntity<ExceptionDetails> createErrorResponse(Exception ex, String title, HttpStatus status) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", ex.getMessage());

        return ResponseEntity.status(status).body(
                new ExceptionDetails(title, LocalDateTime.now(), status.value(), ex.getClass().getName(), errors)
        );
    }
}
