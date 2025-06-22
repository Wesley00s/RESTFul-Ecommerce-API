package org.wesley.ecommerce.application.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            String messageError = error.getDefaultMessage();

            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else {
                fieldName = error.getObjectName();
            }
            errors.put(fieldName, messageError);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDetails(
                        "Bad request! Consult the documentation. Invalid value",
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getObjectName(),
                        errors
                )
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionDetails> handleDataAccessException(DataAccessException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionDetails(
                        "Conflict! Consult the documentation. Not allowed data",
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDetails> handleNoSuchElementException(NoSuchElementException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDetails(
                        "Entity not found! Consult the documentation.",
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleEntityNotFoundException(EntityNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionDetails(
                        "Entity not found! Consult the documentation.",
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ExceptionDetails> handleInsufficientStockException(InsufficientStockException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDetails(
                        "Insufficient stock! Consult the documentation.",
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<ExceptionDetails> handleCartEmptyException(CartEmptyException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDetails(
                        "Cart is empty! Consult the documentation.",
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDetails> handleBusinessException(BusinessException exception) {
        Map<String, String> errors = new HashMap<>();
        String cause = exception.getCause() != null ? exception.getCause().toString() : "No cause available";
        errors.put(cause, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionDetails(
                        "Business rule violation! Consult the documentation.",
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getClass().toString(),
                        errors
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleAllExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String title = "Internal Server Error";

        if (ex instanceof AuthenticationException || ex instanceof AccessDeniedException) {
            return null;
        }

        Map<String, String> errors = new HashMap<>();
        String cause = ex.getCause() != null ? ex.getCause().toString() : "No cause available";
        errors.put(cause, ex.getMessage());

        return ResponseEntity.status(status).body(
                new ExceptionDetails(
                        title,
                        LocalDateTime.now(),
                        status.value(),
                        ex.getClass().getName(),
                        errors
                )
        );
    }
}
