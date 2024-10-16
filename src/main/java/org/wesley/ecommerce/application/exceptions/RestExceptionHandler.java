package org.wesley.ecommerce.application.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestExceptionHandler {

    // Handles validation errors
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

    // Handles DataAccessException
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

    // Handles NoSuchElementException
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

    // Handles EntityNotFoundException
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
}
