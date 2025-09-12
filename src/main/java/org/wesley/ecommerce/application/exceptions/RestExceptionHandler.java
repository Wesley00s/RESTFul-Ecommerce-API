package org.wesley.ecommerce.application.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(
                new ExceptionDetails("Bad Request: Invalid fields", LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getClass().getName(), errors)
        );
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionDetails> handleNotFound(Exception ex) {
        return createErrorResponse(ex, "Resource not found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InsufficientStockException.class, CartEmptyException.class, BusinessException.class})
    public ResponseEntity<ExceptionDetails> handleBusinessExceptions(Exception ex) {
        return createErrorResponse(ex, "Business rule violation.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> handleAccessDenied(AccessDeniedException ex) {
        return createErrorResponse(ex, "Access Denied.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGenericException(Exception ex) {
        return createErrorResponse(ex, "An unexpected internal error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionDetails> createErrorResponse(Exception ex, String title, HttpStatus status) {
        Map<String, String> errors = new HashMap<>();
        errors.put("cause", ex.getMessage());

        return ResponseEntity.status(status).body(
                new ExceptionDetails(title, LocalDateTime.now(), status.value(), ex.getClass().getName(), errors)
        );
    }
}